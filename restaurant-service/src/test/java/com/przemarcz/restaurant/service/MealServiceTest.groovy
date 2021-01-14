package com.przemarcz.restaurant.service


import com.przemarcz.restaurant.model.Meal
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.repository.MealRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.przemarcz.restaurant.dto.MealDto.*

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class MealServiceTest extends Specification {

    @Autowired
    MealService mealService
    @Autowired
    MealRepository mealRepository
    @Autowired
    RestaurantRepository restaurantRepository

    def setup() {
        mealRepository.deleteAll()
        restaurantRepository.deleteAll()
    }

    def "should get all restauant meals without filters from only one restaurant"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Meal meal1 = Meal.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant1.id)
                .build()
        Meal meal2 = Meal.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant1.id)
                .build()
        Meal meal3 = Meal.builder()
                .name("test")
                .id(UUID.randomUUID())
                .restaurantId(restaurant2.id)
                .build()

        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2))
        mealRepository.saveAll(Arrays.asList(meal1, meal2, meal3))
        MealFilter filters = new MealFilter(null, null, null, null, null)
        when:
        MealListResponse responseRestaurant1 = mealService.getAllRestaurantMeals(restaurant1.id, filters)
        MealListResponse responseRestaurant2 = mealService.getAllRestaurantMeals(restaurant2.id, filters)
        then:
        responseRestaurant1.meals.size() == 2
        responseRestaurant2.meals.size() == 1
        responseRestaurant2.meals.get(0).name == "test"
    }

    def "should return empty meals when restaurant meals is empty"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        MealFilter filters = new MealFilter(null, null, null, null, null)
        when:
        MealListResponse response = mealService.getAllRestaurantMeals(restaurant.id, filters)
        then:
        response.meals.size() == 0
    }

    def "should return empty meals when restaurant not found"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        MealFilter filters = new MealFilter(null, null, null, null, null)
        when:
        MealListResponse response = mealService.getAllRestaurantMeals(UUID.randomUUID(), filters)
        then:
        response.meals.size() == 0
    }

    def "should get all restauant meals when filter by category"() {
        given:
        Restaurant restaurnat = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Meal meal1 = Meal.builder()
                .id(UUID.randomUUID())
                .category("pizza")
                .restaurantId(restaurnat.id)
                .build()
        Meal meal2 = Meal.builder()
                .id(UUID.randomUUID())
                .category("pizza")
                .restaurantId(restaurnat.id)
                .build()
        Meal meal3 = Meal.builder()
                .category("soup")
                .id(UUID.randomUUID())
                .restaurantId(restaurnat.id)
                .build()
        Meal meal4 = Meal.builder()
                .category("kebab")
                .id(UUID.randomUUID())
                .restaurantId(restaurnat.id)
                .build()
        restaurantRepository.save(restaurnat)
        mealRepository.saveAll(Arrays.asList(meal1, meal2, meal3, meal4))
        MealFilter filters = new MealFilter("pizza,soup", null, null, null, null)
        when:
        MealListResponse responseRestaurant1 = mealService.getAllRestaurantMeals(restaurnat.id, filters)
        then:
        responseRestaurant1.meals.size() == 3
    }

    def "should get all restauant meals when filter by price"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Meal meal1 = Meal.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(5.21))
                .restaurantId(restaurant.id)
                .build()
        Meal meal2 = Meal.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(3.00))
                .restaurantId(restaurant.id)
                .build()
        Meal meal3 = Meal.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(2.20))
                .restaurantId(restaurant.id)
                .build()
        Meal meal4 = Meal.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(4.00))
                .restaurantId(restaurant.id)
                .build()
        restaurantRepository.save(restaurant)
        mealRepository.saveAll(Arrays.asList(meal1, meal2, meal3, meal4))
        MealFilter filters = new MealFilter(null, fromPrice, toPrice, null, null)
        when:
        MealListResponse response = mealService.getAllRestaurantMeals(restaurant.id, filters)
        then:
        response.meals.size() == size
        where:
        fromPrice                | toPrice                  || size
        BigDecimal.valueOf(3.00) | null                     || 3
        BigDecimal.valueOf(2.99) | BigDecimal.valueOf(4.00) || 2
        null                     | BigDecimal.valueOf(3.00) || 2
    }

    def "should get all restauant meals when filter by time"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Meal meal1 = Meal.builder()
                .id(UUID.randomUUID())
                .timeToDo(BigDecimal.valueOf(2))
                .restaurantId(restaurant.id)
                .build()
        Meal meal2 = Meal.builder()
                .id(UUID.randomUUID())
                .timeToDo(BigDecimal.valueOf(3))
                .restaurantId(restaurant.id)
                .build()
        Meal meal3 = Meal.builder()
                .id(UUID.randomUUID())
                .timeToDo(BigDecimal.valueOf(4))
                .restaurantId(restaurant.id)
                .build()
        Meal meal4 = Meal.builder()
                .id(UUID.randomUUID())
                .timeToDo(BigDecimal.valueOf(5))
                .restaurantId(restaurant.id)
                .build()
        restaurantRepository.save(restaurant)
        mealRepository.saveAll(Arrays.asList(meal1, meal2, meal3, meal4))
        MealFilter filters = new MealFilter(null, null, null, fromTime, toTime)
        when:
        MealListResponse response = mealService.getAllRestaurantMeals(restaurant.id, filters)
        then:
        response.meals.size() == size
        where:
        fromTime              | toTime                || size
        BigDecimal.valueOf(3) | null                  || 3
        BigDecimal.valueOf(2) | BigDecimal.valueOf(4) || 3
        null                  | BigDecimal.valueOf(3) || 2
    }

    def "should get meals category"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Meal meal1 = Meal.builder()
                .id(UUID.randomUUID())
                .category("pizza")
                .restaurantId(restaurant.id)
                .build()
        Meal meal2 = Meal.builder()
                .id(UUID.randomUUID())
                .category("pizza")
                .restaurantId(restaurant.id)
                .build()
        Meal meal3 = Meal.builder()
                .id(UUID.randomUUID())
                .category("soup")
                .restaurantId(restaurant.id)
                .build()
        Meal meal4 = Meal.builder()
                .id(UUID.randomUUID())
                .category("kebab")
                .restaurantId(restaurant.id)
                .build()
        restaurantRepository.save(restaurant)
        mealRepository.saveAll(Arrays.asList(meal1, meal2, meal3, meal4))
        when:
        MealsCategoryResponse response = mealService.getRestaurantMealsCategory(restaurant.id)
        then:
        response.category.size() == 3
    }

    def "should add meal"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        CreateMealRequest createMeal = CreateMealRequest.builder()
                .name("name")
                .category("category")
                .timeToDo(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .build()
        restaurantRepository.save(restaurant)
        when:
        MealResponse response = mealService.addMeal(restaurant.id, createMeal)
        then:
        response != null
        mealRepository.findAll().size() == 1
    }

    def "should update meal"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        Meal meal = mealRepository.save(Meal.builder()
                .id(UUID.randomUUID())
                .name("name")
                .restaurantId(restaurant.id)
                .category("category")
                .timeToDo(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .build())
        UpdateMealRequest updateMeal = UpdateMealRequest.builder()
                .name("updated")
                .category("cat")
                .timeToDo(BigDecimal.valueOf(55))
                .price(BigDecimal.valueOf(12.22))
                .build()
        when:
        MealResponse response = mealService.updateMeal(restaurant.id, meal.id, updateMeal)
        then:
        Meal mealFromDb = mealRepository.findAll().get(0)
        response != null
        mealFromDb.name == "updated"
        mealFromDb.category == "cat"
        mealFromDb.timeToDo == BigDecimal.valueOf(55)
        mealFromDb.price == BigDecimal.valueOf(12.22)
    }

    def "should delete meal from database"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        mealRepository.save(Meal.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id).build())
        Meal mealToDelete = mealRepository.save(Meal.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id).build())
        when:
        mealService.deleteMeal(restaurant.id, mealToDelete.id)
        then:
        mealRepository.findAll().size() == 1
    }
}
