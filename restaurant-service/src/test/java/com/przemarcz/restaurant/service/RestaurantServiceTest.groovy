package com.przemarcz.restaurant.service

import com.przemarcz.restaurant.dto.RestaurantDto
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.model.enums.RestaurantCategory
import com.przemarcz.restaurant.repository.MealRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.awt.print.Pageable

import static org.springframework.data.domain.Pageable.*

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class RestaurantServiceTest extends Specification {

    @Autowired
    RestaurantService restaurantService
    @Autowired
    RestaurantRepository restaurantRepository

    def setup() {
        restaurantRepository.deleteAll()
    }

    Restaurant prepareRestaurant(UUID restaurantId) {
        Restaurant restaurant = new Restaurant()
        restaurant.setId(restaurantId)
        return restaurant
    }

    Restaurant prepareRestaurant(UUID restaurantId,String name, Set category, String city, BigDecimal rate) {
        Restaurant restaurant = new Restaurant()
        restaurant.setName(name)
        restaurant.setCategory(category)
        restaurant.setCity(city)
        restaurant.setRate(rate)
        restaurant.setId(restaurantId)
        return restaurant
    }

    RestaurantDto prepareRestaurantDto(String name, Set category, String city, boolean open, BigDecimal rate) {
        RestaurantDto restaurant = new RestaurantDto()
        restaurant.setName(name)
        restaurant.setCategory(category)
        restaurant.setCity(city)
        restaurant.setOpen(open)
        restaurant.setRate(rate)
        return restaurant
    }

    def "should return all restaurants"(){
        given:
        Restaurant restaurant1 = prepareRestaurant(UUID.randomUUID())
        Restaurant restaurant2 = prepareRestaurant(UUID.randomUUID())
        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        Page<RestaurantDto> restaurants = restaurantService.getAllRestaurants(unpaged(),  new RestaurantDto())
        then:
        restaurants.size==2
    }

    def "should return all restaurants when filter by name"(){
        given:
        Restaurant restaurant1 = prepareRestaurant(UUID.randomUUID(),"name",null,null,null)
        Restaurant restaurant2 = prepareRestaurant(UUID.randomUUID(),"diff",null,null,null)
        RestaurantDto filters = new RestaurantDto()
        filters.setName("na")
        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        Page<RestaurantDto> restaurants = restaurantService.getAllRestaurants(unpaged(), filters)
        then:
        restaurants.size==1
        restaurants.content.get(0).name=="name"
    }

    def "should return all restaurants when filter by city"(){
        given:
        Restaurant restaurant1 = prepareRestaurant(UUID.randomUUID(),null,null,"Gdynia",null)
        Restaurant restaurant2 = prepareRestaurant(UUID.randomUUID(),null,null,"Warszawa",null)
        RestaurantDto filters = new RestaurantDto()
        filters.setCity("SzAw")
        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        Page<RestaurantDto> restaurants = restaurantService.getAllRestaurants(unpaged(), filters)
        then:
        restaurants.size==1
        restaurants.content.get(0).city=="Warszawa"
    }

    def "should return all restaurants when filter by category"(){
        given:
        Set<RestaurantCategory> categories1 = new HashSet<>()
        Set<RestaurantCategory> categories2 = new HashSet<>()

        categories1.add(RestaurantCategory.BURGER)
        categories1.add(RestaurantCategory.SUSHI)

        categories2.add(RestaurantCategory.SUSHI)

        Restaurant restaurant1 = prepareRestaurant(UUID.randomUUID(),null,categories1,null,null)
        Restaurant restaurant2 = prepareRestaurant(UUID.randomUUID(),null,categories2,null,null)
        RestaurantDto filters1 = new RestaurantDto()
        RestaurantDto filters2 = new RestaurantDto()

        Set<RestaurantCategory> categoriesFilter1 = new HashSet<>()
        categoriesFilter1.add(RestaurantCategory.SUSHI)
        filters1.setCategory(categoriesFilter1)

        Set<RestaurantCategory> categoriesFilter2 = new HashSet<>()
        categoriesFilter2.add(RestaurantCategory.BURGER)
        filters2.setCategory(categoriesFilter2)

        restaurantRepository.save(restaurant1)
        restaurantRepository.save(restaurant2)
        when:
        Page<RestaurantDto> restaurants1 = restaurantService.getAllRestaurants(unpaged(), filters1)
        Page<RestaurantDto> restaurants2 = restaurantService.getAllRestaurants(unpaged(), filters2)
        then:
        restaurants1.size==2
        restaurants2.size==1
    }
}
