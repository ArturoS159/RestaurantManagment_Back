package com.przemarcz.order.controller;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final String TOPIC_ORDERS = "orders";
    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PathVariable UUID restaurantId,
                                                       Pageable pageable) {
        return new ResponseEntity<>(orderService.getAllOrders(restaurantId, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/orders/refresh")
    public ResponseEntity<Void> checkOrdersStatus(@PathVariable UUID restaurantId){
        orderService.checkOrdersStatus(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID restaurantId,@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderService.getOrder(restaurantId, orderId), HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/orders/{orderId}/pay")
    public ResponseEntity<OrderDto> payOrderAgain(@PathVariable UUID restaurantId,@PathVariable UUID orderId) throws IOException {
        return new ResponseEntity<>(orderService.payOrderAgain(restaurantId, orderId), HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_ORDERS)
    public void consumeOrders(ConsumerRecord<String, OrderAvro> orderAvro) throws IOException {
        orderService.addOrder(orderAvro);
    }
}
