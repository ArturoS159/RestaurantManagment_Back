package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.dto.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.MealMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.MealRepository;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {

    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Transactional(value = "transactionManager", readOnly = true)
    public List<MealDto> getRestaurantMeals(UUID restaurantId) {
        return getRestaurant(restaurantId)
                .getMealList().stream()
                .map(mealMapper::toMealDto).
                        collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager")
    public void addMeal(UUID restaurantId, MealDto mealDto) {
        Restaurant restaurant = getRestaurant(restaurantId);
        Meal meal = mealMapper.toMeal(mealDto, restaurantId);
        restaurant.addMeal(meal);
        restaurantRepository.save(restaurant);
    }

    private Restaurant getRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(value = "transactionManager")
    public void updateMeal(UUID restaurantId, UUID mealId, MealDto mealDto) {
        Meal meal = getMeal(restaurantId, mealId);
        mealMapper.updateMeal(mealDto, meal);
        mealRepository.save(meal);
    }

    @Transactional(value = "transactionManager")
    public void deleteMeal(UUID restaurantId, UUID mealId) {
        Meal meal = getMeal(restaurantId, mealId);
        mealRepository.delete(meal);
    }

    private Meal getMeal(UUID restaurantId, UUID mealId) {
        return mealRepository.findByIdAndRestaurantId(mealId, restaurantId)
                .orElseThrow(() -> new NotFoundException("Meal not found!"));
    }
}
