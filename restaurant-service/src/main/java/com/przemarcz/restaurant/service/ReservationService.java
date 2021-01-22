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
        restaurant.checkReservationTime(checkReservationStatusRequest.getDay(), checkReservationStatusRequest.getFrom(), checkReservationStatusRequest.getTo());
        List<Table> tablesInRestaurant = getRestaurantTables(restaurant, checkReservationStatusRequest.getNumberOfSeats());
        List<UUID> tablesId = tablesInRestaurant.stream().map(Table::getId).collect(Collectors.toList());
        if (tablesInRestaurant.isEmpty()) {
            throw new NotFoundException("No tables found");
        }
        List<Reservation> reservations = getRestaurantReservationsInThisDay(checkReservationStatusRequest.getDay(), checkReservationStatusRequest.getFrom(), checkReservationStatusRequest.getTo(), tablesId);
        List<Table> availableTables = getAvailableTables(tablesInRestaurant, reservations);

        if (!availableTables.isEmpty()) {
            return new CheckReservationStatusResponse(true, Collections.emptyList(), availableTables);
        }
        Map<UUID, List<Time>> tablesAndTime = new TreeMap<>();

        List<Time> startedTime = new ArrayList<>();
        startedTime.add(restaurant.getWorkTimeOfDay());

        tablesId.forEach(uuid -> tablesAndTime.put(uuid, startedTime));

        for (Reservation reservation : reservations) {
            List<Time> time = tablesAndTime.get(reservation.getTableId());
            time.add(new Time(reservation.getFrom(), reservation.getTo()));
            tablesAndTime.put(reservation.getTableId(), time);
        }

        List<CheckReservationResponse> finalList = new ArrayList<>();

        for (Map.Entry<UUID, List<Time>> ss : tablesAndTime.entrySet()) {
            List<Time> timesss = ss.getValue();
            finalList.add(new CheckReservationResponse(ss.getKey(), timesss));
        }
        return new CheckReservationStatusResponse(false, finalList, availableTables);
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
