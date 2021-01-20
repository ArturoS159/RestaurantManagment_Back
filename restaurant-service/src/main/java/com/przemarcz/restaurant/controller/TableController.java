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
    @GetMapping("/{restaurantId}/tables")
    public ResponseEntity<TablesResponse> getTables(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(tableService.getTables(restaurantId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/tables")
    public ResponseEntity<TablesResponse> addTables(@PathVariable UUID restaurantId,
                                                    @RequestBody CreateTablesRequest createTablesRequest) {
        return new ResponseEntity<>(tableService.addTables(restaurantId, createTablesRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/tables")
    public ResponseEntity<TablesResponse> updateTables(@PathVariable UUID restaurantId,
                                                       @RequestBody UpdateTablesRequest updateTablesRequest) {
        return new ResponseEntity<>(tableService.updateTables(restaurantId, updateTablesRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/tables")
    public ResponseEntity<Void> deleteTables(@PathVariable UUID restaurantId, @RequestParam int size) {
        tableService.deleteTables(restaurantId, size);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
