package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.mapper.MealMapper;
import com.przemarcz.restaurant.model.Meal;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.MealRepository;
import com.przemarcz.restaurant.specification.MealSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.restaurant.dto.MealDto.*;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final RestaurantService restaurantService;

    @Transactional(value = "transactionManager", readOnly = true)
    public MealListResponse getAllRestaurantMeals(UUID restaurantId, MealFilter mealFilter) {
        MealSpecification mealSpecification = new MealSpecification(restaurantId, mealFilter);
        List<Meal> meals = mealRepository.findAll(mealSpecification);
        List<MealResponse> mealsResponse = meals.stream()
                .map(mealMapper::toMealResponse)
                .collect(Collectors.toList());
        return new MealListResponse(mealsResponse);
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public MealsCategoryResponse getRestaurantMealsCategory(UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Set<String> categories = restaurant.getMeals().stream()
                .map(Meal::getCategory)
                .collect(Collectors.toSet());
        return new MealsCategoryResponse(categories);
    }

    @Transactional(value = "transactionManager")
    public MealResponse addMeal(UUID restaurantId, CreateMealRequest mealRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Meal meal = mealMapper.toMeal(mealRequest, restaurantId);
        restaurant.addMeal(meal);
        return mealMapper.toMealResponse(meal);
    }

    @Transactional(value = "transactionManager")
    public MealResponse updateMeal(UUID restaurantId, UUID mealId, UpdateMealRequest mealDto) {
        Meal meal = restaurantService
                .getRestaurantFromDatabase(restaurantId)
                .getMeal(mealId);
        mealMapper.updateMeal(meal, mealDto);
        mealRepository.save(meal);
        return mealMapper.toMealResponse(meal);
    }

    @Transactional(value = "transactionManager")
    public void deleteMeal(UUID restaurantId, UUID mealId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        restaurant.deleteMeal(mealId);
    }
}
