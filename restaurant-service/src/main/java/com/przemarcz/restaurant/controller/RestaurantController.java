package com.przemarcz.restaurant.controller;

import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    private static final String TOPIC_PAYMENT = "payments";
    private final RestaurantService restaurantService;

    @GetMapping("/public")
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurant(Pageable pageable, @ModelAttribute("restaurantDto") RestaurantDto restaurantDto){
        return new ResponseEntity<>(restaurantService.getAllRestaurants(pageable, restaurantDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurantForOwner(Principal user, Pageable pageable){
        return new ResponseEntity<>(restaurantService.getAllRestaurantForOwner(user.getName(), pageable), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/public")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(restaurantService.getRestaurant(restaurantId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> addRestaurant(Principal user, @RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.addRestaurant(user.getName(), restaurantDto),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable UUID restaurantId,
                                                 @RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.updateRestaurant(restaurantId, restaurantDto),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/time")
    public ResponseEntity<Void> updateRestaurantTime(@PathVariable UUID restaurantId, @RequestBody List<WorkTimeDto> worksTime) {
        restaurantService.updateRestaurantTime(restaurantId, worksTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> delRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/order")
    public ResponseEntity<Void> orderMeals(Principal principal, @PathVariable UUID restaurantId, @RequestBody OrderDto orderDto) {
        restaurantService.orderMeals(restaurantId, orderDto, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/order-personal")
    public ResponseEntity<Void> orderMealsByPersonal(@PathVariable UUID restaurantId, @RequestBody OrderDto orderDto) {
        restaurantService.orderMealsByPersonal(restaurantId, orderDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/payment")
    public ResponseEntity<Boolean> isPaymentAvailable(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(restaurantService.isPaymentAvailable(restaurantId),HttpStatus.OK);
    }

    @KafkaListener(topics = TOPIC_PAYMENT)
    public void consumeOrders(ConsumerRecord<String, PaymentAvro> paymentAvro){
        restaurantService.addOrDeletePayment(paymentAvro);
    }
}
