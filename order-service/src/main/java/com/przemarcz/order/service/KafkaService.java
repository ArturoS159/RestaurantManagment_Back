package com.przemarcz.order.service;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.helper.OrderHelper;
import com.przemarcz.order.mapper.OrderMapper;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KafkaService {

    private static final String TOPIC_ORDERS = "orders";
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderHelper orderHelper;

    @KafkaListener(topics = TOPIC_ORDERS)
    @Transactional("chainedKafkaTransactionManager")
    public void consumeFromOwnerTopic(ConsumerRecord<String, OrderAvro> orderAvro) {
        Order order = orderMapper.toOrder(orderAvro.value());
        order.setPrice(orderHelper.countOrderPrice(order.getMeals()));
        orderRepository.save(order);
    }
}
