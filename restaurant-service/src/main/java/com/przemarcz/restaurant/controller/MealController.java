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

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.MealDto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class MealController {

    private final MealService mealService;
    private final KafkaTemplate<String, OrderAvro> orderKafkaTemplate;

    @GetMapping("/{restaurantId}/meals")
    public ResponseEntity<List<MealResponse>> getRestaurantMeals(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(mealService.getAllRestaurantMeals(restaurantId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/meals")
    public ResponseEntity<MealResponse> addMeal(@PathVariable UUID restaurantId,
                                                @Valid @RequestBody MealDto.CreateMealRequest meal) {
        return new ResponseEntity<>(mealService.addMeal(restaurantId, meal),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/meals/{mealId}")
    public ResponseEntity<MealResponse> updateMeal(@PathVariable UUID restaurantId,
                                                   @PathVariable UUID mealId,
                                                   @Valid @RequestBody MealDto.UpdateMealRequest mealRequest) {
        return new ResponseEntity<>(mealService.updateMeal(restaurantId, mealId, mealRequest),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/meals/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable UUID restaurantId,
                                           @PathVariable UUID mealId) {
        mealService.deleteMeal(restaurantId, mealId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
