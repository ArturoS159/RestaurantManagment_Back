package com.przemarcz.order.controller;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.dto.PayUDto.PayUUrlResponse;
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
import java.security.Principal;
import java.util.UUID;

import static com.przemarcz.order.dto.OrderDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final String TOPIC_ORDERS = "orders";
    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<Page<OrderForRestaurantResponse>> getAllOrders(@PathVariable UUID restaurantId,
                                                                         @ModelAttribute OrderFilter orderFilter,
                                                                         Pageable pageable) {
        return new ResponseEntity<>(orderService.getAllOrders(restaurantId, orderFilter, pageable), HttpStatus.OK);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderForUserResponse>> getAllOrdersForUser(Principal user, Pageable pageable) {
        return new ResponseEntity<>(orderService.getAllOrdersForUser(user.getName(), pageable), HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/orders/refresh")
    public ResponseEntity<Void> refreshOrdersStatus(@PathVariable UUID restaurantId){
        orderService.refreshOrdersStatus(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/orders/{orderId}/pay")
    public ResponseEntity<PayUUrlResponse> payOrder(@PathVariable UUID restaurantId, @PathVariable UUID orderId) throws IOException {
        return new ResponseEntity<>(orderService.payOrder(restaurantId, orderId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<OrderForRestaurantResponse> updateOrder(@PathVariable UUID restaurantId,
                                                                  @PathVariable UUID orderId,
                                                                  @RequestBody UpdateOrderRequest updateOrderRequest){
        return new ResponseEntity<>(orderService.updateOrder(restaurantId, orderId, updateOrderRequest), HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_ORDERS)
    public void consumeOrders(ConsumerRecord<String, OrderAvro> orderAvro) {
        orderService.addOrder(orderAvro.value());
    }
}
