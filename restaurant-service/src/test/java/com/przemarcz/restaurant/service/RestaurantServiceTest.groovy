package com.przemarcz.restaurant.service

import com.google.common.collect.Sets
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.model.enums.RestaurantCategory
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.przemarcz.restaurant.dto.RestaurantDto.*
import static org.springframework.data.domain.Pageable.unpaged

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

    def "should return all restaurants without filters"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        RestaurantFilter fitlers = new RestaurantFilter(null, null, null, null, null)
        when:
        Page<AllRestaurantResponse> response = restaurantService.getAllRestaurants(fitlers, unpaged())
        then:
        response.size == 3
    }

    def "should return all restaurants filtered by name"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("AA")
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("AB")
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("CC")
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        RestaurantFilter fitlers1 = new RestaurantFilter("a", null, null, null, null)
        RestaurantFilter fitlers2 = new RestaurantFilter("c", null, null, null, null)
        when:
        Page<AllRestaurantResponse> response1 = restaurantService.getAllRestaurants(fitlers1, unpaged())
        Page<AllRestaurantResponse> response2 = restaurantService.getAllRestaurants(fitlers2, unpaged())
        then:
        response1.size == 2
        response2.size == 1
    }

    def "should return all restaurants filtered by category"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .category("PIZZA,BURGER")
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .category("BURGER")
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .category("KEBAB")
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        RestaurantFilter fitlers1 = new RestaurantFilter(null, Sets.newHashSet(RestaurantCategory.BURGER), null, null, null)
        RestaurantFilter fitlers2 = new RestaurantFilter(null, Sets.newHashSet(RestaurantCategory.KEBAB), null, null, null)
        RestaurantFilter fitlers3 = new RestaurantFilter(null, Sets.newHashSet(RestaurantCategory.BURGER, RestaurantCategory.KEBAB), null, null, null)
        when:
        Page<AllRestaurantResponse> response1 = restaurantService.getAllRestaurants(fitlers1, unpaged())
        Page<AllRestaurantResponse> response2 = restaurantService.getAllRestaurants(fitlers2, unpaged())
        Page<AllRestaurantResponse> response3 = restaurantService.getAllRestaurants(fitlers3, unpaged())
        then:
        response1.size == 2
        response2.size == 1
        response3.size == 0
    }

    def "should return all restaurants filtered by city"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .city("Tarnow")
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .city("Warszawa")
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .city("Waclawek")
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        RestaurantFilter fitlers1 = new RestaurantFilter(null, null, "tar", null, null)
        RestaurantFilter fitlers2 = new RestaurantFilter(null, null, "wa", null, null)
        when:
        Page<AllRestaurantResponse> response1 = restaurantService.getAllRestaurants(fitlers1, unpaged())
        Page<AllRestaurantResponse> response2 = restaurantService.getAllRestaurants(fitlers2, unpaged())
        then:
        response1.size == 1
        response2.size == 2
    }

    def "should return all restaurants filtered by rate"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .rate(BigDecimal.valueOf(3.2))
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .rate(BigDecimal.valueOf(2.67))
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .rate(BigDecimal.valueOf(3.0))
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        RestaurantFilter fitlers1 = new RestaurantFilter(null, null, null, BigDecimal.valueOf(3), null)
        RestaurantFilter fitlers2 = new RestaurantFilter(null, null, null, BigDecimal.valueOf(3.2), null)
        when:
        Page<AllRestaurantResponse> response1 = restaurantService.getAllRestaurants(fitlers1, unpaged())
        Page<AllRestaurantResponse> response2 = restaurantService.getAllRestaurants(fitlers2, unpaged())
        then:
        response1.size == 2
        response2.size == 1
    }

    def "should return correct restaurant by id"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        Restaurant restaurant1 = Restaurant.builder()
                .id(restaurantId)
                .name("r1")
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("r1")
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2))
        when:
        RestaurantResponse response = restaurantService.getRestaurant(restaurantId)
        then:
        response.name == "r1"
    }

    def "should return all restaurants for owner"() {
        given:
        UUID ownerId = UUID.randomUUID()
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .build()
        Restaurant restaurant3 = Restaurant.builder()
                .id(UUID.randomUUID())
                .ownerId(UUID.randomUUID())
                .build()
        RestaurantFilter fitlers = new RestaurantFilter(null, null, null, null, null)
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2, restaurant3))
        when:
        Page<AllRestaurantForOwnerResponse> response = restaurantService.getAllRestaurantForOwner(fitlers, ownerId.toString(), unpaged())
        then:
        response.size == 2
    }

    def "should return correct restaurant for owner by id"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        Restaurant restaurant1 = Restaurant.builder()
                .id(restaurantId)
                .name("r1")
                .build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("r1")
                .build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2))
        when:
        RestaurantForOwnerResponse response = restaurantService.getRestaurantForOwner(restaurantId)
        then:
        response.name == "r1"
    }
}
