package com.przemarcz.restaurant.service

import com.przemarcz.restaurant.dto.OpinionDto
import com.przemarcz.restaurant.model.Opinion
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.repository.OpinionRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.springframework.data.domain.Pageable.unpaged

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class OpinionServiceTest extends Specification {

    @Autowired
    OpinionService opinionService
    @Autowired
    OpinionRepository opinionRepository
    @Autowired
    RestaurantRepository restaurantRepository


    def setup() {
        opinionRepository.deleteAll()
        restaurantRepository.deleteAll()
    }

    Restaurant prepareRestaurant(UUID restaurantId) {
        Restaurant restaurant = new Restaurant()
        restaurant.setId(restaurantId)
        return restaurant
    }

    Opinion prepareOpinion(BigDecimal rate) {
        Opinion opinion = new Opinion()
        opinion.setRate(rate)
        return opinion
    }

    OpinionDto prepareOpinionDto(BigDecimal rate) {
        OpinionDto opinion = new OpinionDto()
        opinion.setRate(rate)
        return opinion
    }

    def "should add opinion to restaurant when it's first opinion"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Restaurant restaurant = prepareRestaurant(restaurantId)
        restaurantRepository.save(restaurant)
        OpinionDto opinion = prepareOpinionDto(new BigDecimal("3.21"))
        when:
        opinionService.addRestaurantOpinion(opinion, restaurantId, userId.toString())
        then:
        opinionRepository.findAll().size() == 1
        restaurantRepository.findAll().get(0).rate == new BigDecimal("3.21")
        opinionService.getAllRestaurantOpinions(restaurantId, unpaged()).content.get(0).rate == new BigDecimal("3.0")
    }

    def "should add opinion and count restaurant rate when it's second opinion"() {
        given:
        UUID restaurantId = UUID.randomUUID()
        Restaurant restaurant = prepareRestaurant(restaurantId)
        restaurantRepository.save(restaurant)
        OpinionDto opinion1 = prepareOpinionDto(new BigDecimal("3.0"))
        OpinionDto opinion2 = prepareOpinionDto(new BigDecimal("5.0"))
        when:
        opinionService.addRestaurantOpinion(opinion1, restaurantId, UUID.randomUUID().toString())
        opinionService.addRestaurantOpinion(opinion2, restaurantId, UUID.randomUUID().toString())
        then:
        opinionRepository.findAll().size() == 2
        restaurantRepository.findAll().get(0).rate == new BigDecimal("4.0")
    }
}
