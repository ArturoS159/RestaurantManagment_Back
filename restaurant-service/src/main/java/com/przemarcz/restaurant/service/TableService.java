package com.przemarcz.restaurant.service;

import com.przemarcz.restaurant.mapper.TableReservationMapper;
import com.przemarcz.restaurant.model.Restaurant;
import com.przemarcz.restaurant.model.Table;
import com.przemarcz.restaurant.repository.RestaurantRepository;
import com.przemarcz.restaurant.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.restaurant.dto.TableReservationDto.*;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantRepository restaurantRepository;
    private final TableReservationMapper tableReservationMapper;
    private final RestaurantService restaurantService;
    private final TableRepository tableRepository;

    @Transactional(value = "transactionManager", readOnly = true)
    public TablesResponse getTables(UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        List<TableResponse> tablesResponse = restaurant.getTables()
                .stream().map(tableReservationMapper::toTableResponse)
                .collect(Collectors.toList());
        return new TablesResponse(tablesResponse);
    }

    @Transactional(value = "transactionManager")
    public TablesResponse addTables(UUID restaurantId, CreateTablesRequest createTablesRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        List<Table> tables = createTablesRequest.getTables().stream()
                .map(tableReservationMapper::toTable)
                .collect(Collectors.toList());
        restaurant.addTables(tables);
        restaurantRepository.save(restaurant);

        List<TableResponse> tablesResponse = tables.stream().map(tableReservationMapper::toTableResponse).collect(Collectors.toList());
        return new TablesResponse(tablesResponse);
    }

    @Transactional(value = "transactionManager")
    public TablesResponse updateTables(UUID restaurantId, UpdateTablesRequest tablesRequest) {
        Restaurant restaurant = restaurantService.getRestaurantFromDatabase(restaurantId);
        List<Table> tables = restaurant.getTables();
        List<Table> tablesResponseTmp = new ArrayList<>();
        for (UpdateTableRequest updateTable : tablesRequest.getTables()) {
            for (Table table : tables) {
                if (updateTable.getId().equals(table.getId())) {
                    tablesResponseTmp.add(table);
                    tableReservationMapper.updateTable(table, updateTable);
                }
            }
        }
        return new TablesResponse(tablesResponseTmp.stream().map(tableReservationMapper::toTableResponse).collect(Collectors.toList()));
    }

    @Transactional(value = "transactionManager")
    public void deleteTables(UUID restaurantId, int size) {
        tableRepository.deleteByRestaurantIdAndNumberOfSeats(restaurantId, size);
    }
}
