package com.przemarcz.restaurant.service

import com.przemarcz.restaurant.dto.MealDto
import com.przemarcz.restaurant.model.Meal
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.repository.MealRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

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
        restaurantRepository.deleteAll()
        mealRepository.deleteAll()
    }

    Restaurant prepareRestaurant(UUID restaurantId) {
        Restaurant restaurant = new Restaurant()
        restaurant.setId(restaurantId)
        return restaurant
    }

    Meal prepareMeal(String name, BigDecimal price) {
        Meal meal = new Meal()
        meal.setName(name)
        meal.setPrice(price)
        return meal
    }

    MealDto prepareMealDto(String name, BigDecimal price) {
        MealDto meal = new MealDto()
        meal.setName(name)
        meal.setPrice(price)
        return meal
    }

    def "should get all restaurant meals from only one restaurant"() {
        given:
        UUID restaurantId1 = UUID.randomUUID()
        UUID restaurantId2 = UUID.randomUUID()
        Restaurant restaurant1 = prepareRestaurant(restaurantId1)
        Restaurant restaurant2= prepareRestaurant(restaurantId2)
        restaurant1.meals << prepareMeal("Meal1", new BigDecimal("23.5"))
        restaurant1.meals << prepareMeal("Meal2", new BigDecimal("25.5"))
        restaurant2.meals << prepareMeal("Meal2", new BigDecimal("1.50"))
        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        List<MealDto> meals = mealService.getAllRestaurantMeals(restaurantId1, mealFilter, pageable)
        List<MealDto> meals2 = mealService.getAllRestaurantMeals(restaurantId2, mealFilter, pageable)
        then:
        meals.size()==2
        meals2.size()==1
    }

    def "should add meal"() {
        given:
        UUID restaurantId1 = UUID.randomUUID()
        UUID restaurantId2 = UUID.randomUUID()
        Restaurant restaurant1 = prepareRestaurant(restaurantId1)
        Restaurant restaurant2 = prepareRestaurant(restaurantId2)
        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        mealService.addMeal(restaurantId1,prepareMealDto("Meal1", new BigDecimal("3.21")))
        mealService.addMeal(restaurantId1,prepareMealDto("Meal2", new BigDecimal("1")))
        mealService.addMeal(restaurantId2,prepareMealDto("Meal2", new BigDecimal("2.22")))
        then:
        mealRepository.findAll().size()==3
        mealService.getAllRestaurantMeals(restaurantId1, mealFilter, pageable).size()==2
        mealService.getAllRestaurantMeals(restaurantId2, mealFilter, pageable).size()==1
    }

    def "should update meal"() {
        given:
        UUID restaurantId1 = UUID.randomUUID()
        Restaurant restaurant = prepareRestaurant(restaurantId1)
        Meal meal = prepareMeal("Meal", new BigDecimal("3.21"))
        Meal meal1 = prepareMeal("Meal", new BigDecimal("3.21"))
        restaurant.meals << meal
        restaurant.meals << meal1
        restaurantRepository.save(restaurant)
        MealDto updateMeal = prepareMealDto("MealUpdated", new BigDecimal("5.2"))
        when:
        mealService.updateMeal(restaurantId1,meal.id,updateMeal)
        then:
        mealRepository.findAll().size()==2
        mealService.getAllRestaurantMeals(restaurantId1, mealFilter, pageable).get(0).name=="MealUpdated"
        mealService.getAllRestaurantMeals(restaurantId1, mealFilter, pageable).get(0).price.toString() == "5.20"
    }

    def "should delete meal"() {
        given:
        UUID restaurantId1 = UUID.randomUUID()
        Restaurant restaurant = prepareRestaurant(restaurantId1)
        Meal meal = prepareMeal("Meal1", new BigDecimal("3.21"))
        Meal meal1 = prepareMeal("Meal2", new BigDecimal("3.52"))
        restaurant.meals << meal
        restaurant.meals << meal1
        restaurantRepository.save(restaurant)
        when:
        mealService.deleteMeal(restaurantId1,meal.getId())
        then:
        mealRepository.findAll().size()==1
        mealService.getAllRestaurantMeals(restaurantId1, mealFilter, pageable).size()==1
    }
}
