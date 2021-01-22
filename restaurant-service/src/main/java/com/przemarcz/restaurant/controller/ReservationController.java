package com.przemarcz.restaurant.controller;

import com.przemarcz.restaurant.dto.TableReservationDto;
import com.przemarcz.restaurant.dto.TableReservationDto.CheckReservationStatusRequest;
import com.przemarcz.restaurant.dto.TableReservationDto.MyReservationResponse;
import com.przemarcz.restaurant.dto.TableReservationDto.ReservationResponse;
import com.przemarcz.restaurant.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.TableReservationDto.CheckReservationStatusResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{restaurantId}/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@PathVariable UUID restaurantId,
                                                              @RequestBody TableReservationDto.CreateReservationRequest createReservationRequest,
                                                              Principal user) {
        return new ResponseEntity<>(reservationService.addReservation(restaurantId, createReservationRequest, user.getName()), HttpStatus.CREATED);
    }

    @PostMapping("/{restaurantId}/reservations/status")
    public ResponseEntity<CheckReservationStatusResponse> checkReservationStatus(@PathVariable UUID restaurantId,
                                                                                 @RequestBody CheckReservationStatusRequest checkReservationStatusRequest) {
        return new ResponseEntity<>(reservationService.checkReservationStatus(restaurantId, checkReservationStatusRequest), HttpStatus.OK);
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<Page<MyReservationResponse>> getMyReservations(Principal user, Pageable pageable) {
        return new ResponseEntity<>(reservationService.getMyReservations(user.getName(), pageable), HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/reservations")
    public ResponseEntity<Page<ReservationResponse>> getRestaurantReservations(@RequestParam(required = false)
                                                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,
                                                                               Pageable pageable,
                                                                               Principal principal) {
        return new ResponseEntity<>(reservationService.getRestaurantReservations(day, pageable), HttpStatus.OK);
    }
}
