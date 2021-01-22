package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.TableReservationDto.*;
import com.przemarcz.restaurant.model.Reservation;
import com.przemarcz.restaurant.model.Table;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = {BigDecimal.class, UUID.class})
public interface TableReservationMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Table toTable(CreateTableRequest createTablesRequest);

    TableResponse toTableResponse(Table table);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTable(@MappingTarget Table table, UpdateTableRequest updateTableRequest);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Reservation toReservation(UUID restaurantId, CreateReservationRequest createReservationRequest, UUID userId, String restaurantName, String tableName, Integer sizeOfTable);

    ReservationResponse toReservationResponse(Reservation reservation);

    MyReservationResponse toMyReservationResponse(Reservation reservations);

    CheckReservationStatusRequest convertToCheck(CreateReservationRequest reservationRequest);
}
