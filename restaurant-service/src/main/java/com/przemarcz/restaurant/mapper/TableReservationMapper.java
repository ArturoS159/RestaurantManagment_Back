package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.TableReservationDto;
import com.przemarcz.restaurant.dto.TableReservationDto.CreateTableRequest;
import com.przemarcz.restaurant.dto.TableReservationDto.TableResponse;
import com.przemarcz.restaurant.model.Reservation;
import com.przemarcz.restaurant.model.Table;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

import static com.przemarcz.restaurant.dto.TableReservationDto.*;

@Mapper(componentModel = "spring", uses = MealMapper.class, imports = BigDecimal.class)
public interface TableReservationMapper {
    Table toTable(CreateTableRequest createTableRequest);
    TableResponse toTableResponse(Table table);
    Reservation toReservation(AddReservationRequest addReservationRequest);

}
