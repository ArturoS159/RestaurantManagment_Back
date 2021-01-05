package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.TableReservationDto;
import com.przemarcz.restaurant.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/tables")
    public ResponseEntity<TableReservationDto.TableResponse> addTable(@PathVariable UUID restaurantId,
                                                                      @RequestBody TableReservationDto.CreateTableRequest createTableRequest) {
        return new ResponseEntity<>(tableService.addTable(restaurantId, createTableRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/tables/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID restaurantId) {
        tableService.deleteTable(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
