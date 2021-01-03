package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.TableReservationDto.CreateTableRequest;
import com.przemarcz.restaurant.dto.TableReservationDto.AddReservationRequest;
import com.przemarcz.restaurant.dto.TableReservationDto.TableResponse;
import com.przemarcz.restaurant.service.TableReservationService;
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
public class TableReservationController {

    private final TableReservationService tableReservationService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId/tables")
    public ResponseEntity<TableResponse> addTable(@PathVariable UUID restaurantId,
                                                  @RequestBody CreateTableRequest createTableRequest) {
        return new ResponseEntity<>(tableReservationService.addTable(restaurantId, createTableRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId/tables/reservations")
    public ResponseEntity<TableResponse> addReservation(@PathVariable UUID restaurantId,
                                                               @RequestBody AddReservationRequest addReservationRequest){
        return new ResponseEntity<>(tableReservationService.addReservation(restaurantId, addReservationRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId/tables/{tableId}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID restaurantId) {
        tableReservationService.deleteTable(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
