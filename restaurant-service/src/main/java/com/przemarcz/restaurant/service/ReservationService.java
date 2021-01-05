package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.mapper.TableReservationMapper;
import com.przemarcz.restaurant.model.Reservation;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.AddReservationRequest;
import static com.przemarcz.restaurant.dto.TableReservationDto.ReservationResponse;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RestaurantRepository restaurantRepository;
    private final TableReservationMapper tableReservationMapper;
    private final RestaurantService restaurantService;

    @Transactional(value = "transactionManager")
    public ReservationResponse addReservation(UUID restaurantId, AddReservationRequest addReservationRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Reservation reservation = tableReservationMapper.toReservation(addReservationRequest);
        restaurant.createReservation(addReservationRequest.getNumberOfSeats(), reservation);
        restaurantRepository.save(restaurant);
        return tableReservationMapper.toReservationResponse(reservation);
    }
}
