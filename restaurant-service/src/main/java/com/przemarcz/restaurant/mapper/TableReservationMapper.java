package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.TableReservationDto.CreateTableRequest;
import com.przemarcz.restaurant.dto.TableReservationDto.ReservationResponse;
import com.przemarcz.restaurant.dto.TableReservationDto.TableResponse;
import com.przemarcz.restaurant.model.Reservation;
import com.przemarcz.restaurant.model.Table;
import org.mapstruct.*;

import java.math.BigDecimal;

import static com.przemarcz.restaurant.dto.TableReservationDto.AddReservationRequest;
import static com.przemarcz.restaurant.dto.TableReservationDto.UpdateTableRequest;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = BigDecimal.class)
public interface TableReservationMapper {
    Table toTable(CreateTableRequest createTableRequest);

    TableResponse toTableResponse(Table table);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTable(@MappingTarget Table table, UpdateTableRequest updateTableRequest);

    Reservation toReservation(AddReservationRequest addReservationRequest);

    ReservationResponse toReservationResponse(Reservation reservation);
}
