package com.przemarcz.restaurant.service

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class RestaurantServiceTest extends Specification {

}
