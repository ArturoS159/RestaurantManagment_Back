package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.dto.TableReservationDto.*;
import com.przemarcz.restaurant.exception.NotFoundException;
import com.przemarcz.restaurant.mapper.TableReservationMapper;
import com.przemarcz.restaurant.mapper.TextMapper;
import com.przemarcz.restaurant.model.Reservation;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.Table;
import com.przemarcz.restaurant.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableReservationMapper tableReservationMapper;
    private final TextMapper textMapper;
    private final RestaurantService restaurantService;

    @Transactional(value = "transactionManager")
    public ReservationResponse addReservation(UUID restaurantId, CreateReservationRequest createReservationRequest, String userId) {
//        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
//        restaurant.checkReservationTime(createReservationRequest.getDay(), createReservationRequest.getFrom(), createReservationRequest.getTo());
//        List<Table> tablesInRestaurants = getTablesRestaurant(createReservationRequest.getNumberOfSeats(), restaurant);
//
//        if (CollectionUtils.isEmpty(tablesInRestaurants)) {
//            throw new NotFoundException("No tables available found!");
//        }
//
////        List<Reservation> reservationsInThisDay = getRestaurantReservationsInThisDay(createReservationRequest.getDay(), createReservationRequest.getFrom(), createReservationRequest.getTo());
//        List<Table> availableTables = getAvailableTables(tablesInRestaurants, reservationsInThisDay);
//
//        if (CollectionUtils.isEmpty(availableTables)) {
//            throw new NotFoundException("No tables available found!");
//        }
//        Reservation reservation = tableReservationMapper.toReservation(restaurantId, createReservationRequest, textMapper.toUUID(userId));
//        Table table = availableTables.get(new Random().nextInt(availableTables.size()));
//        table.addReservation(reservation);
//        return tableReservationMapper.toReservationResponse(reservation);
        return null;
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public CheckReservationStatusResponse checkReservationStatus(UUID restaurantId, CheckReservationStatusRequest checkReservationStatusRequest) {
//        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
//        List<Table> tablesInRestaurant = getTablesRestaurant(checkReservationStatusRequest.getNumberOfSeats(), restaurant);
//        if (tablesInRestaurant.isEmpty()) {
//            return tableReservationMapper.toCheckReservationStatusResponse(false, Collections.emptyList());
//        }
////        List<Reservation> reservations = getRestaurantReservationsInThisDay(checkReservationStatusRequest.getDay(), checkReservationStatusRequest.getFrom(), checkReservationStatusRequest.getTo());
//        List<Table> availableTables = getAvailableTables(tablesInRestaurant, reservations);
//
//        if (!availableTables.isEmpty()) {
//            return tableReservationMapper.toCheckReservationStatusResponse(true, Collections.emptyList());
//        }
//        return tableReservationMapper.toCheckReservationStatusResponse(false, reservations);
        return null;
    }

    private List<Table> getTablesRestaurant(int size, Restaurant restaurant) {
        return restaurant.getTables().stream()
                .filter(Table::getCanReserve)
                .filter(table -> table.getNumberOfSeats().equals(size))
                .collect(Collectors.toList());
    }

    private List<Table> getAvailableTables(List<Table> tablesInRestaurant, List<Reservation> reservationsInThisDay) {
        return tablesInRestaurant.stream()
                .filter(table -> reservationsInThisDay
                        .stream()
                        .map(Reservation::getTableId).noneMatch(uuid -> table.getId().equals(uuid)))
                .distinct()
                .collect(Collectors.toList());
    }

//    private List<Reservation> getRestaurantReservationsInThisDay(LocalDate day, UUID restauranId) {
////        return reservationRepository.findAllByDay(day)
////                .stream()
////                .filter(reservation -> reservation.get)
//        return null;
//    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<MyReservationResponse> getMyReservations(String userId, Pageable pageable) {
        return reservationRepository.findAllByUserId(textMapper.toUUID(userId), pageable)
                .map(reservation -> {
                    Restaurant restaurant = restaurantService.getRestaurantFromDatabase(reservation.getRestaurantId());
                    return tableReservationMapper.toMyReservationResponse(restaurant.getName(), reservation);
                });
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<ReservationResponse> getRestaurantReservations(LocalDate day, Pageable pageable) {
        if (isNull(day)) {
            day = LocalDate.now();
        }
        return reservationRepository.findAllByDay(day, pageable).map(
                tableReservationMapper::toReservationResponse
        );
    }
}
