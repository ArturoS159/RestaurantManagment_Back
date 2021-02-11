package com.przemarcz.restaurant.service

import com.przemarcz.restaurant.exception.NotFoundException
import com.przemarcz.restaurant.model.Reservation
import com.przemarcz.restaurant.model.Restaurant
import com.przemarcz.restaurant.model.Table
import com.przemarcz.restaurant.repository.ReservationRepository
import com.przemarcz.restaurant.repository.RestaurantRepository
import com.przemarcz.restaurant.repository.TableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

import static com.przemarcz.restaurant.dto.TableReservationDto.*
import static org.springframework.data.domain.Pageable.unpaged

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:bootstrap-test.yml")
class ReservationServiceTest extends Specification {

    @Autowired
    ReservationService reservationService
    @Autowired
    RestaurantRepository restaurantRepository
    @Autowired
    ReservationRepository reservationRepository
    @Autowired
    TableRepository tableRepository

    def setup() {
        restaurantRepository.deleteAll()
        reservationRepository.deleteAll()
        tableRepository.deleteAll()
    }

    def "should add reservation to restaurant when reservations are empty"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Table table = Table.builder()
                .id(UUID.randomUUID())
                .numberOfSeats(4)
                .restaurantId(restaurant.id)
                .canReserve(true)
                .build()
        restaurantRepository.save(restaurant)
        tableRepository.save(table)
        CreateReservationRequest reservationRequest = CreateReservationRequest.builder()
                .numberOfSeats(4)
                .day(LocalDate.parse("2020-01-01"))
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        when:
        reservationService.addReservation(restaurant.id, reservationRequest, UUID.randomUUID().toString())
        then:
        reservationRepository.findAll().size() == 1
    }

    def "should throw exception when tables not found"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        CreateReservationRequest reservationRequest = CreateReservationRequest.builder()
                .numberOfSeats(4)
                .day(LocalDate.parse("2020-01-01"))
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        when:
        reservationService.addReservation(restaurant.id, reservationRequest, UUID.randomUUID().toString())
        then:
        thrown NotFoundException
    }

    def "should thrown exception when tables not found"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        restaurantRepository.save(restaurant)
        CheckReservationStatusRequest reservationRequest = CheckReservationStatusRequest.builder()
                .numberOfSeats(4)
                .day(LocalDate.parse("2020-01-01"))
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        when:
        reservationService.checkReservationStatus(restaurant.id, reservationRequest)
        then:
        thrown NotFoundException
    }

    def "should return true when reservation time is ok"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Table table = Table.builder()
                .id(UUID.randomUUID())
                .numberOfSeats(4)
                .restaurantId(restaurant.id)
                .canReserve(true)
                .build()
        restaurantRepository.save(restaurant)
        tableRepository.save(table)
        CheckReservationStatusRequest reservationRequest = CheckReservationStatusRequest.builder()
                .numberOfSeats(4)
                .day(LocalDate.parse("2020-01-01"))
                .from(LocalTime.parse("20:00"))
                .to(LocalTime.parse("21:00"))
                .build()
        when:
        CheckReservationStatusResponse response = reservationService.checkReservationStatus(restaurant.id, reservationRequest)
        then:
        response.status
    }

    def "should return my reservations"() {
        given:
        UUID userId = UUID.randomUUID()
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Table table = Table.builder()
                .id(UUID.randomUUID())
                .numberOfSeats(4)
                .restaurantId(restaurant.id)
                .canReserve(true)
                .build()
        Reservation reservation1 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .userId(userId)
                .day(LocalDate.parse("2020-10-10"))
                .build()
        Reservation reservation2 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .userId(userId)
                .day(LocalDate.parse("2020-10-10"))
                .build()
        Reservation reservation3 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .userId(UUID.randomUUID())
                .day(LocalDate.parse("2020-10-11"))
                .build()
        restaurantRepository.save(restaurant)
        tableRepository.save(table)
        reservationRepository.saveAll(Arrays.asList(reservation1, reservation2, reservation3))
        when:
        Page<MyReservationResponse> response = reservationService.getMyReservations(userId.toString(), unpaged())
        then:
        response.size() == 2
    }

    def "should return reservations from specific day"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Table table = Table.builder()
                .id(UUID.randomUUID())
                .numberOfSeats(4)
                .restaurantId(restaurant.id)
                .canReserve(true)
                .build()
        Reservation reservation1 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.parse("2020-10-10"))
                .build()
        Reservation reservation2 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.parse("2020-10-10"))
                .build()
        Reservation reservation3 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.parse("2020-10-11"))
                .build()
        restaurantRepository.save(restaurant)
        tableRepository.save(table)
        reservationRepository.saveAll(Arrays.asList(reservation1, reservation2, reservation3))
        when:
        Page<ReservationResponse> response1 = reservationService.getRestaurantReservations(LocalDate.parse("2020-10-10"), unpaged())
        Page<ReservationResponse> response2 = reservationService.getRestaurantReservations(LocalDate.parse("2020-10-11"), unpaged())
        then:
        response1.size() == 2
        response2.size() == 1
    }

    def "should return reservations from today when day is not specified"() {
        given:
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .build()
        Table table = Table.builder()
                .id(UUID.randomUUID())
                .numberOfSeats(4)
                .restaurantId(restaurant.id)
                .canReserve(true)
                .build()

        Reservation reservation1 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.now())
                .build()
        Reservation reservation2 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.now())
                .build()
        Reservation reservation3 = Reservation.builder()
                .id(UUID.randomUUID())
                .restaurantId(restaurant.id)
                .day(LocalDate.parse("2020-10-11"))
                .build()
        restaurantRepository.save(restaurant)
        tableRepository.save(table)
        reservationRepository.saveAll(Arrays.asList(reservation1, reservation2, reservation3))
        when:
        Page<ReservationResponse> response1 = reservationService.getRestaurantReservations(null, unpaged())
        Page<ReservationResponse> response2 = reservationService.getRestaurantReservations(LocalDate.parse("2020-10-11"), unpaged())
        then:
        response1.size() == 2
        response2.size() == 1
    }
}
