package com.przemarcz.order.service;

import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.mapper.OrderMapper;
import com.przemarcz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Page<OrderDto> getAllOrders(UUID restaurantId, Pageable pageable) {
        return orderRepository.findAllByRestaurantId(restaurantId, pageable).map(orderMapper::toOrderDto);
    }
}
