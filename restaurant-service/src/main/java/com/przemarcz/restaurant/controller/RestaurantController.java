package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.dto.WorkTimeDto;
import com.przemarcz.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurant(Principal user, Pageable pageable,
                                                                @RequestParam(required = false) boolean my) {
        return new ResponseEntity<>(restaurantService.getAllRestaurants(user.getName(), pageable, my), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(restaurantService.getRestaurant(restaurantId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<UUID> addRestaurant(Principal user, @RequestBody RestaurantDto restaurantDto) {
        restaurantService.addRestaurant(user.getName(), restaurantDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable UUID restaurantId,
                                                 @RequestBody RestaurantDto restaurantDto) {
        restaurantService.updateRestaurant(restaurantId, restaurantDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/time")
    public ResponseEntity<Void> updateRestaurantTime(@PathVariable UUID restaurantId, @RequestBody List<WorkTimeDto> worksTime) {
        restaurantService.updateRestaurantTime(restaurantId, worksTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> delRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/orders")
    public ResponseEntity<Void> orderMeals(@PathVariable UUID restaurantId, @RequestBody OrderDto orderDto) {
        restaurantService.orderMeals(restaurantId, orderDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
