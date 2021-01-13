package com.przemarcz.restaurant.service

import com.przemarcz.restaurant.exception.AlreadyExistException
import com.przemarcz.restaurant.model.Opinion
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.repository.OpinionRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.przemarcz.restaurant.dto.OpinionDto.CreateOpinionRequest
import static com.przemarcz.restaurant.dto.OpinionDto.OpinionResponse
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

    def "should get all restaurant opinions"() {
        given:
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID()).build()
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID()).build()
        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2))
        Opinion opinion1 = Opinion.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant1.id)
                .build()
        Opinion opinion2 = Opinion.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant2.id)
                .build()
        Opinion opinion3 = Opinion.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant2.id)
                .build()
        opinionRepository.saveAll(Arrays.asList(opinion1, opinion2, opinion3))
        when:
        List<OpinionResponse> opinions1 = opinionService.getAllRestaurantOpinions(restaurant1.id, unpaged()).asList()
        List<OpinionResponse> opinions2 = opinionService.getAllRestaurantOpinions(restaurant2.id, unpaged()).asList()
        then:
        opinions1.size() == 1
        opinions2.size() == 2
    }

    def "should add opinion to restaurant"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID()).build()
        restaurantRepository.save(restaurant)
        CreateOpinionRequest createOpinion = CreateOpinionRequest.builder()
                .rate(BigDecimal.valueOf(2.5))
                .build()
        when:
        opinionService.addRestaurantOpinion(createOpinion, restaurant.id, UUID.randomUUID().toString())
        then:
        restaurantRepository.findAll().get(0).rate.toString()=="2.50"
        opinionRepository.findAll().size()==1
    }

    def "should not add opinion to restaurant when user added before"() {
        given:
        String userId = UUID.randomUUID()
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID()).build()
        restaurantRepository.save(restaurant)
        CreateOpinionRequest createOpinion1 = CreateOpinionRequest.builder()
                .rate(BigDecimal.valueOf(2.5))
                .build()
        CreateOpinionRequest createOpinion2 = CreateOpinionRequest.builder()
                .rate(BigDecimal.valueOf(2.5))
                .build()
        opinionService.addRestaurantOpinion(createOpinion1, restaurant.id, userId)
        when:
        opinionService.addRestaurantOpinion(createOpinion2, restaurant.id, userId)
        then:
        thrown AlreadyExistException
    }
}
