package com.przemarcz.restaurant.controller;

import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.restaurant.dto.OrderDto;
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

import static com.przemarcz.restaurant.dto.OrderDto.*;

@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final String TOPIC_PAYMENT = "payments";
    private final OrderService orderService;

    @PostMapping("/{restaurantId}/order")
    public ResponseEntity<Void> orderMeals(@PathVariable UUID restaurantId,
                                           @Valid @RequestBody CreateOrderUserRequest createOrderUserRequest,
                                           Principal principal) {
        orderService.orderMeals(restaurantId, createOrderUserRequest, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/order-personal")
    public ResponseEntity<Void> orderMealsByPersonal(@PathVariable UUID restaurantId,
                                                     @RequestBody CreateOrderPersonalRequest createOrderPersonalRequest) {
        orderService.orderMealsByPersonal(restaurantId, createOrderPersonalRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/payment")
    public ResponseEntity<Boolean> isPaymentAvailable(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(orderService.isPaymentAvailable(restaurantId),HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_PAYMENT)
    public void consumeOrders(ConsumerRecord<String, PaymentAvro> paymentAvro){
        orderService.addOrDeletePayment(paymentAvro.value());
    }
}
