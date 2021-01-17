package com.przemarcz.auth.controller;

import com.przemarcz.auth.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.przemarcz.auth.domain.dto.UserDto.WorkerResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkerController {

    private final WorkerService workerService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/workers")
    public ResponseEntity<Page<WorkerResponse>> getAllRestaurantWorkers(@PathVariable UUID restaurantId,
                                                                        Pageable pageable) {
        return new ResponseEntity<>(workerService.getAllRestaurantWorkers(restaurantId, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/workers")
    public ResponseEntity<WorkerResponse> addRestaurantWorker(@PathVariable UUID restaurantId,
                                                              @RequestParam String email) {
        return new ResponseEntity<>(workerService.addRestaurantWorker(restaurantId, email), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/workers/{workerId}")
    public ResponseEntity<Void> deleteRestaurantWorker(@PathVariable UUID restaurantId,
                                                       @PathVariable UUID workerId) {
        workerService.deleteRestaurantWorker(restaurantId, workerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
