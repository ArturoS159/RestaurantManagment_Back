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
import com.przemarcz.order.util.payumodels.Payment;
import com.przemarcz.order.util.PaymentHelper;
import com.przemarcz.order.util.payumodels.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class OrderService {

    private static final int ORDER_MAX_TIME = 15;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final OrderHelper orderHelper;
    private final PaymentHelper paymentHelper;
    private final PayUMapper payUMapper;

    public Page<OrderDto> getAllOrders(UUID restaurantId, Pageable pageable) {
        return orderRepository.findAllByRestaurantId(restaurantId, pageable).map(orderMapper::toOrderDto);
    }

    public void checkOrdersStatus(UUID restaurantId) {
        List<Order> orders = orderRepository.findAllByRestaurantId(restaurantId);
        //TODO get orders from today delete orders expired
        RestaurantPayment restaurantPayment = getRestaurantPayment(restaurantId);
        List<Order> ordersNew = orders.stream()
                .filter(order -> !order.isPayed())
                .filter(this::isOrderNotExpired)
                .filter(order -> isOrderPayed(restaurantPayment,order.getPayUOrderId())
        ).collect(Collectors.toList());
        ordersNew.forEach(order -> order.setPayed(true));
        orderRepository.saveAll(ordersNew);
    }

    private boolean isOrderPayed(RestaurantPayment restaurantPayment, String payUOrderId){
        return paymentHelper.checkOrderStatus(restaurantPayment,payUOrderId);
    }

    private boolean isOrderNotExpired(Order order) {
        LocalDateTime orderTime = order.getTime();
        return orderTime.plusMinutes(ORDER_MAX_TIME).isBefore(orderTime);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public OrderDto getOrder(UUID restaurantId, UUID orderId) {
        return orderMapper.toOrderDto(getOrderFromDb(restaurantId, orderId));
    }

    @Transactional("chainedKafkaTransactionManager")
    public void addOrder(ConsumerRecord<String, OrderAvro> orderAvro) {
        Order order = orderMapper.toOrder(orderAvro.value());
        order.setPrice(orderHelper.countOrderPrice(order.getMeals()));
        orderRepository.save(order);
    }

    public OrderDto payOrderAgain(UUID restaurantId, UUID orderId) throws IOException {
        Order order = getOrderFromDb(restaurantId,orderId);
        if(order.isPayed()){
            throw new IllegalArgumentException("");
            //TODO
        }
        orderMapper.updateOrderPayUResponse(order,preparePayment(order));
        return orderMapper.toOrderDto(order);
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

    private Order getOrderFromDb(UUID restaurantId, UUID orderId) {
        return orderRepository.findByRestaurantIdAndId(restaurantId,orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s in restaurant %s not found!",orderId,restaurantId)));
    }
}
