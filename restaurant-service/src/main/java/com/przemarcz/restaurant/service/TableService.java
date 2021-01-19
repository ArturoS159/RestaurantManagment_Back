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
    public TableResponse updateTable(UUID restaurantId, UUID tableId, CreateUpdateTableRequest updateTableRequest) {
        Table table = tableRepository.findByIdAndRestaurantId(tableId, restaurantId).orElseThrow(NotFoundException::new);
        //tableReservationMapper.updateTable(table,updateTableRequest);
        tableRepository.save(table);
        return tableReservationMapper.toTableResponse(table);
    }

    @Transactional(value = "transactionManager")
    public void deleteTable(UUID restaurantId, UUID tableId) {
        Table table = tableRepository.findByIdAndRestaurantId(tableId, restaurantId).orElseThrow(NotFoundException::new);
        tableRepository.delete(table);
    }
}
