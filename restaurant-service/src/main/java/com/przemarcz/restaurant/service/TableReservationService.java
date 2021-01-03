package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.dto.TableReservationDto.TableResponse;
import com.przemarcz.restaurant.mapper.TableReservationMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.Table;
import com.przemarcz.restaurant.repository.TableReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.*;
import static com.przemarcz.restaurant.dto.TableReservationDto.CreateTableRequest;

@Service
@RequiredArgsConstructor
public class TableReservationService {

    private final TableReservationRepository tableReservationRepository;
    private final TableReservationMapper tableReservationMapper;
    private final RestaurantService restaurantService;

    public TableResponse addTable(UUID restaurantId, CreateTableRequest createTableRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Table table = tableReservationMapper.toTable(createTableRequest);
        restaurant.createTable(table);
        return tableReservationMapper.toTableResponse(table);
    }

    public ReservationTableResponse addReservation(UUID restaurantId, AddReservationRequest addReservationRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        tableReservationRepository.findAllByNumberOfSeatsAndReservationsDay(addReservationRequest.getNumberOfSeats(), addReservationRequest.getDay());
        //TODO
    }

    public void deleteTable(UUID restaurantId) {
        //TODO if is it have reservation not delete
        tableReservationRepository.deleteById(restaurantId);
    }
}
