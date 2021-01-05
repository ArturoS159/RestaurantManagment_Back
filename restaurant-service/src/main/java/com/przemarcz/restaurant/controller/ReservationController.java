package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.TableReservationDto.AddReservationRequest;
import com.przemarcz.restaurant.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.ReservationResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@PathVariable UUID restaurantId,
                                                              @RequestBody AddReservationRequest addReservationRequest) {
        return new ResponseEntity<>(reservationService.addReservation(restaurantId, addReservationRequest), HttpStatus.CREATED);
    }
}
