package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.mapper.TableReservationMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.Table;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import com.przemarcz.restaurant.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.*;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantRepository restaurantRepository;
    private final TableReservationMapper tableReservationMapper;
    private final RestaurantService restaurantService;
    private final TableRepository tableRepository;

    @Transactional(value = "transactionManager")
    public TableResponse addTable(UUID restaurantId, CreateTableRequest createTableRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        Table table = tableReservationMapper.toTable(createTableRequest);
        restaurant.createTable(table);
        restaurantRepository.save(restaurant);
        return tableReservationMapper.toTableResponse(table);
    }

    @Transactional(value = "transactionManager")
    public TableResponse updateTable(UUID restaurantId, UUID tableId, UpdateTableRequest updateTableRequest) {
        Table table = tableRepository.findByIdAndRestaurantId(tableId,restaurantId).orElseThrow(NotFoundException::new);
        tableReservationMapper.updateTable(table,updateTableRequest);
        tableRepository.save(table);
        return tableReservationMapper.toTableResponse(table);
    }

    @Transactional(value = "transactionManager")
    public void deleteTable(UUID restaurantId) {
        tableRepository.deleteById(restaurantId);
    }
}
