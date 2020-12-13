package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.RestaurantDto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/public")
    public ResponseEntity<Page<AllRestaurantResponse>> getAllRestaurant(@ModelAttribute("restaurantFilter") RestaurantFilter restaurantFilter,
                                                                        Pageable pageable){
        return new ResponseEntity<>(restaurantService.getAllRestaurants(restaurantFilter, pageable), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AllRestaurantOwnerResponse>> getAllRestaurantForOwner(@ModelAttribute("restaurantFilter") RestaurantFilter restaurantFilter,
                                                                                     Principal user,
                                                                                     Pageable pageable){
        return new ResponseEntity<>(restaurantService.getAllRestaurantForOwner(restaurantFilter, user.getName(), pageable), HttpStatus.OK);
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
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> delRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
