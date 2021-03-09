package com.przemarcz.restaurant.controller;

import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.restaurant.dto.OrderDto.CreateOrderUserRequest;
import com.przemarcz.restaurant.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderPersonalRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final String TOPIC_PAYMENT = "payments";
    private final OrderService orderService;

    @PostMapping("/{restaurantId}/order")
    public ResponseEntity<CreateOrderResponse> orderMealsByClient(@PathVariable UUID restaurantId,
                                                   @Valid @RequestBody CreateOrderUserRequest createOrderUserRequest,
                                                   Principal principal) {
        return new ResponseEntity<>(orderService.orderMealsByClient(restaurantId, createOrderUserRequest, principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/order-personal")
    public ResponseEntity<CreateOrderResponse> orderMealsByPersonal(@PathVariable UUID restaurantId,
                                                     @Valid @RequestBody CreateOrderPersonalRequest createOrderPersonalRequest) {
        return new ResponseEntity<>(orderService.orderMealsByPersonal(restaurantId, createOrderPersonalRequest), HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_PAYMENT)
    public void consumePayment(ConsumerRecord<String, PaymentAvro> paymentAvro){
        orderService.addOrDeletePayment(paymentAvro.value());
    }
}
