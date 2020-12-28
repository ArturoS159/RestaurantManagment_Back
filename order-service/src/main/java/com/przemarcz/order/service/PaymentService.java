package com.przemarcz.order.service;

import com.przemarcz.avro.AddDelete;
import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.exception.AlreadyExistException;
import com.przemarcz.order.exception.NotFoundException;
import com.przemarcz.order.mapper.PaymentMapper;
import com.przemarcz.order.model.RestaurantPayment;
import com.przemarcz.order.repository.PaymentRepository;
import com.przemarcz.order.util.PaymentHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentHelper paymentHelper;
    private final KafkaTemplate<String, PaymentAvro> paymentKafkaTemplate;

    @Transactional("chainedKafkaTransactionManager")
    public PaymentDto addPayment(UUID restaurantId, PaymentDto paymentDto){
        if(hasRestaurantPayment(restaurantId)){
            throw new AlreadyExistException("This restaurant has payment!");
        }
        if(isWrongPaymentDetails(paymentDto)){
            throw new IllegalArgumentException("Payment not valid!");
        }
        RestaurantPayment restaurantPayment = paymentMapper.toPayment(paymentDto, restaurantId);
        paymentRepository.save(restaurantPayment);
        sendPayment(restaurantId, AddDelete.ADD);
//        return paymentMapper.toPaymentDto(restaurantPayment);
        return null;
    }

    private boolean hasRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId).isPresent();
    }

    private boolean isWrongPaymentDetails(PaymentDto paymentDto) {
        return false;
//        return isNull(paymentHelper.getAuthorizationToken(paymentDto.getClientId(),paymentDto.getClientSecret()));
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public PaymentDto getPayment(UUID restaurantId) {
        return null;
//        return paymentMapper.toPaymentDto(getRestaurantPayment(restaurantId));
    }

    @Transactional(value = "transactionManager")
    public PaymentDto updatePayment(UUID restaurantId, PaymentDto paymentDto) {
        RestaurantPayment restaurantPayment = getRestaurantPayment(restaurantId);
        paymentMapper.updatePayment(restaurantPayment, paymentDto, restaurantId);
        if(isWrongPaymentDetails(paymentDto)){
            throw new IllegalArgumentException("Payment not valid!");
        }
        paymentRepository.save(restaurantPayment);
//        return paymentMapper.toPaymentDto(restaurantPayment);
        return null;
    }

    @Transactional(value = "transactionManager")
    public void deletePayment(UUID restaurantId) {
        if(hasRestaurantPayment(restaurantId)){
            paymentRepository.deleteById(restaurantId);
            sendPayment(restaurantId,AddDelete.DEL);
        }
    }

    private RestaurantPayment getRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with %s not found!", restaurantId)));
    }

    private void sendPayment(UUID restaurantId, AddDelete type){
        PaymentAvro paymentAvro = paymentMapper.toPaymentAvro(restaurantId,type);
        paymentKafkaTemplate.send("payments", paymentAvro);
    }
}
