package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/tables")
    public ResponseEntity<TableResponse> addTable(@PathVariable UUID restaurantId,
                                                  @RequestBody CreateTableRequest createTableRequest) {
        return new ResponseEntity<>(tableService.addTable(restaurantId, createTableRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/tables/{tableId}")
    public ResponseEntity<TableResponse> updateTable(@PathVariable UUID restaurantId,
                                                     @PathVariable UUID tableId,
                                                     @RequestBody UpdateTableRequest updateTableRequest) {
        return new ResponseEntity<>(tableService.updateTable(restaurantId, tableId, updateTableRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/tables/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID restaurantId, @PathVariable UUID tableId) {
        tableService.deleteTable(restaurantId, tableId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
