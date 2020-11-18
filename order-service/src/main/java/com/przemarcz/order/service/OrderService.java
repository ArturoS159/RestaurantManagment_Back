package com.przemarcz.order.service;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.exception.NotFoundException;
import com.przemarcz.order.mapper.OrderMapper;
import com.przemarcz.order.mapper.PayUMapper;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.model.RestaurantPayment;
import com.przemarcz.order.repository.OrderRepository;
import com.przemarcz.order.repository.PaymentRepository;
import com.przemarcz.order.util.OrderHelper;
import com.przemarcz.order.util.Payment;
import com.przemarcz.order.util.PaymentHelper;
import com.przemarcz.order.util.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderHelper orderHelper;
    private final PaymentHelper paymentHelper;
    private final PayUMapper payUMapper;
    private final PaymentRepository paymentRepository;

    public Page<OrderDto> getAllOrders(UUID restaurantId, Pageable pageable) {
        return orderRepository.findAllByRestaurantId(restaurantId, pageable).map(orderMapper::toOrderDto);
    }

    @Transactional("chainedKafkaTransactionManager")
    public void addOrder(ConsumerRecord<String, OrderAvro> orderAvro) throws IOException {
        Order order = orderMapper.toOrder(orderAvro.value());
        order.setPrice(orderHelper.countOrderPrice(order.getMeals()));
        orderMapper.updateOrderPayUResponse(order,preparePayment(order));
        orderRepository.save(order);
    }

    private PaymentResponse preparePayment(Order order) throws IOException {
        RestaurantPayment restaurantPayment;
        restaurantPayment = getRestaurantPayment(order.getRestaurantId());

        Payment payment = payUMapper.toPayment(order, restaurantPayment.getPosId());
        return paymentHelper.pay(payment,restaurantPayment);
    }

    private RestaurantPayment getRestaurantPayment(UUID restaurantId) {
        return paymentRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment for restaurant %s not found!", restaurantId)));
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public OrderDto getOrder(UUID restaurantId, UUID orderId) {
        return orderMapper.toOrderDto(orderRepository.findByRestaurantIdAndId(restaurantId,orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s in restaurant %s not found!",orderId,restaurantId))));
    }
}
