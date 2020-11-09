package com.przemarcz.restaurant.controller;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.service.MealService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class MealController {

    private final MealService mealService;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;

    @GetMapping("/{restaurantId}/meals")
    public ResponseEntity<List<MealDto>> getRestaurantMeals(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(mealService.getAllRestaurantMeals(restaurantId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/meals")
    public ResponseEntity<Void> addMeal(@PathVariable UUID restaurantId,
                                        @RequestBody MealDto mealDto) {
        mealService.addMeal(restaurantId, mealDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/meals/{mealId}")
    public ResponseEntity<Void> updateMeal(@PathVariable UUID restaurantId,
                                           @PathVariable UUID mealId,
                                           @RequestBody MealDto mealDto) {
        mealService.updateMeal(restaurantId, mealId, mealDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/meals/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable UUID restaurantId,
                                           @PathVariable UUID mealId) {
        mealService.deleteMeal(restaurantId, mealId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
