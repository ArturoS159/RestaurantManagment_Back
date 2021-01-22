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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
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
        CheckReservationStatusResponse statusResponse = checkReservationStatus(restaurantId, tableReservationMapper.convertToCheck(createReservationRequest));
        if (statusResponse.isStatus()) {
            Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
            Table tableToReserve = getRandomTable(statusResponse);
            Reservation reservation = tableReservationMapper.toReservation(restaurantId, createReservationRequest, textMapper.toUUID(userId), restaurant.getName(), tableToReserve.getName(), tableToReserve.getNumberOfSeats());
            restaurant.addReservation(tableToReserve.getId(), reservation);
            return tableReservationMapper.toReservationResponse(reservation);
        }
        throw new IllegalArgumentException("Cant reserve");
    }

    private Table getRandomTable(CheckReservationStatusResponse statusResponse) {
        return statusResponse.getTables().get(new Random().nextInt(statusResponse.getTables().size()));
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public CheckReservationStatusResponse checkReservationStatus(UUID restaurantId, CheckReservationStatusRequest checkReservationStatusRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        restaurant.checkReservationTime(checkReservationStatusRequest);
        List<Table> tablesInRestaurant = getRestaurantTables(restaurant, checkReservationStatusRequest.getNumberOfSeats());
        if (tablesInRestaurant.isEmpty()) {
            throw new NotFoundException("No tables found");
        }

        List<UUID> tablesId = tablesInRestaurant.stream().map(Table::getId).collect(Collectors.toList());
        List<Reservation> reservations = getRestaurantReservationsInThisDay(checkReservationStatusRequest.getDay(), checkReservationStatusRequest.getFrom(), checkReservationStatusRequest.getTo(), tablesId);
        List<Table> availableTables = getAvailableTables(tablesInRestaurant, reservations);

        if (!availableTables.isEmpty()) {
            return new CheckReservationStatusResponse(true, Collections.emptyList(), availableTables);
        }

        Map<UUID, List<LocalTime>> tablesAndTime = new HashMap<>();

        for (Reservation reservation : reservations) {
            tablesAndTime.computeIfAbsent(reservation.getTableId(), k -> new ArrayList<>()).add(reservation.getFrom());
            tablesAndTime.computeIfAbsent(reservation.getTableId(), k -> new ArrayList<>()).add(reservation.getTo());
        }

        tablesAndTime.forEach((key, value) -> value.addAll(restaurant.getWorkTimeOfDay()));
        tablesAndTime.forEach((key, value) -> Collections.sort(value));

        List<CheckReservationResponse> listToReturn = getSortedTimeList(tablesAndTime);

        return new CheckReservationStatusResponse(false, listToReturn, availableTables);
    }

    private List<CheckReservationResponse> getSortedTimeList(Map<UUID, List<LocalTime>> tablesAndTime) {
        List<CheckReservationResponse> listToReturn = new ArrayList<>();
        tablesAndTime.forEach((key, value) -> {
            List<Time> listOfTimes = new ArrayList<>();
            for (int i = 0; i < value.size(); i += 2) {
                listOfTimes.add(new Time(value.get(i), value.get(i + 1)));
            }
            listToReturn.add(new CheckReservationResponse(key, listOfTimes));
        });
        return listToReturn;
    }

    private List<Table> getRestaurantTables(Restaurant restaurant, int size) {
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

    private List<Reservation> getRestaurantReservationsInThisDay(LocalDate day, LocalTime from, LocalTime to, List<UUID> tablesId) {
        return reservationRepository.findAllByDayAndTableIdIn(day, tablesId)
                .stream()
                .filter(reservation -> !reservation.isReservationOpen(from, to))
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public Page<MyReservationResponse> getMyReservations(String userId, Pageable pageable) {
        return reservationRepository.findAllByUserId(textMapper.toUUID(userId), pageable).map(tableReservationMapper::toMyReservationResponse);
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
