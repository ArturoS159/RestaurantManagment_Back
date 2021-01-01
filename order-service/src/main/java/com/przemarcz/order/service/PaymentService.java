package com.przemarcz.order.service;

import com.przemarcz.avro.AddDelete;
import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.order.dto.PaymentDto.CreatePaymentRequest;
import com.przemarcz.order.dto.PaymentDto.PaymentResponse;
import com.przemarcz.order.exception.AlreadyExistException;
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
    public PaymentResponse addPayment(UUID restaurantId, CreatePaymentRequest paymentDto){
        if(hasRestaurantPayment(restaurantId)){
            throw new AlreadyExistException("This restaurant has payment!");
        }
        if(isWrongPaymentDetails(paymentDto)){
            throw new IllegalArgumentException("Payment not valid!");
        }
        RestaurantPayment restaurantPayment = paymentMapper.toPayment(paymentDto, restaurantId);
        paymentRepository.save(restaurantPayment);
        sendPaymentToRestaurantService(restaurantId, AddDelete.ADD);
        return paymentMapper.toPaymentResponse(restaurantPayment);
    }

    private boolean hasRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId).isPresent();
    }

    private boolean isWrongPaymentDetails(CreatePaymentRequest createPaymentRequest) {
        return isNull(paymentHelper.getAuthorizationToken(createPaymentRequest.getClientId(),createPaymentRequest.getClientSecret()));
    }

    @Transactional(value = "transactionManager")
    public void deletePayment(UUID restaurantId) {
        if(hasRestaurantPayment(restaurantId)){
            paymentRepository.deleteById(restaurantId);
            sendPaymentToRestaurantService(restaurantId,AddDelete.DEL);
        }
    }

    private void sendPaymentToRestaurantService(UUID restaurantId, AddDelete type){
        PaymentAvro paymentAvro = paymentMapper.toPaymentAvro(restaurantId,type);
        paymentKafkaTemplate.send("payments", paymentAvro);
    }
}
