package com.przemarcz.order.service;

import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.exception.AlreadyExistException;
import com.przemarcz.order.exception.NotFoundException;
import com.przemarcz.order.mapper.PaymentMapper;
import com.przemarcz.order.model.RestaurantPayment;
import com.przemarcz.order.repository.PaymentRepository;
import com.przemarcz.order.util.PaymentHelper;
import lombok.RequiredArgsConstructor;
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

    @Transactional(value = "transactionManager")
    public PaymentDto addPayment(UUID restaurantId, PaymentDto paymentDto){
        if(hasRestaurantPayment(restaurantId)){
            throw new AlreadyExistException("This restaurant has payment!");
        }
        if(isWrongPaymentDetails(paymentDto)){
            throw new IllegalArgumentException("Payment not valid!");
        }
        RestaurantPayment restaurantPayment = paymentMapper.toPayment(paymentDto, restaurantId);
        paymentRepository.save(restaurantPayment);
        //TODO send kafka to restaurant-service added payment
        return paymentMapper.toPaymentDto(restaurantPayment);
    }

    private boolean hasRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId).isPresent();
    }

    private boolean isWrongPaymentDetails(PaymentDto paymentDto) {
        return isNull(paymentHelper.getAuthorizationToken(paymentDto.getClientId(),paymentDto.getClientSecret()));
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public PaymentDto getPayment(UUID restaurantId) {
        return paymentMapper.toPaymentDto(getRestaurantPayment(restaurantId));
    }

    @Transactional(value = "transactionManager")
    public PaymentDto updatePayment(UUID restaurantId, PaymentDto paymentDto) {
        RestaurantPayment restaurantPayment = getRestaurantPayment(restaurantId);
        paymentMapper.updatePayment(restaurantPayment, paymentDto, restaurantId);
        if(isWrongPaymentDetails(paymentDto)){
            throw new IllegalArgumentException("Payment not valid!");
        }
        paymentRepository.save(restaurantPayment);
        return paymentMapper.toPaymentDto(restaurantPayment);
    }

    @Transactional(value = "transactionManager")
    public void deletePayment(UUID restaurantId) {
        if(hasRestaurantPayment(restaurantId)){
            paymentRepository.deleteById(restaurantId);
        }
    }

    private RestaurantPayment getRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with %s not found!", restaurantId)));
    }
}
