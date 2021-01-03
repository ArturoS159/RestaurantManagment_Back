package com.przemarcz.restaurant.controller;

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

import static com.przemarcz.restaurant.dto.RestaurantDto.*;
import static com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeResponse;

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

    @GetMapping("/{restaurantId}/public")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(restaurantService.getRestaurant(restaurantId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AllRestaurantOwnerResponse>> getAllRestaurantForOwner(@ModelAttribute("restaurantFilter") RestaurantFilter restaurantFilter,
                                                                                     Principal user,
                                                                                     Pageable pageable){
        return new ResponseEntity<>(restaurantService.getAllRestaurantForOwner(restaurantFilter, user.getName(), pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantOwnerResponse> getRestaurantForOwner(@PathVariable UUID restaurantId){
        return new ResponseEntity<>(restaurantService.getRestaurantForOwner(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/time/public")
    public ResponseEntity<List<WorkTimeResponse>> getRestaurantWorkTime(@PathVariable UUID restaurantId){
        return new ResponseEntity<>(restaurantService.getRestaurantWorkTime(restaurantId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantOwnerResponse> addRestaurant(@RequestBody CreateRestaurantRequest createRestaurantRequest,
                                                                 Principal user) {
        return new ResponseEntity<>(restaurantService.addRestaurant(createRestaurantRequest, user.getName()),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantOwnerResponse> updateRestaurant(@PathVariable UUID restaurantId,
                                                                    @RequestBody UpdateRestaurantRequest updateRestaurantRequest) {
        return new ResponseEntity<>(restaurantService.updateRestaurant(restaurantId, updateRestaurantRequest),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> delRestaurant(@PathVariable UUID restaurantId) {
        restaurantService.delRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
