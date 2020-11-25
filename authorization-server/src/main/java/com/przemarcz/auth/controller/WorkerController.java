package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.service.WorkerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkerController {

    private final WorkerService workerService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/workers")
    public ResponseEntity<Page<UserDto>> getAllRestaurantWorkers(@PathVariable UUID restaurantId,
                                                                 Pageable pageable) {
        return new ResponseEntity<>(workerService.getAllRestaurantWorkers(restaurantId, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/workers")
    public ResponseEntity<Void> addRestaurantWorker(@PathVariable UUID restaurantId,
                                                    @RequestParam String email) {
        workerService.addRestaurantWorker(restaurantId,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/workers/{workerId}")
    public ResponseEntity<Page<UserDto>> getAllRestaurantWorkers(@PathVariable UUID restaurantId, @PathVariable UUID workerId) {
        return null;
    }
}
