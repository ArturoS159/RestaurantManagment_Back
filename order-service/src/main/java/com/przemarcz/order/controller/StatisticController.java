package com.przemarcz.order.controller;

import com.przemarcz.order.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.przemarcz.order.dto.StatisticDto.StatisticFilter;
import static com.przemarcz.order.dto.StatisticDto.StatisticResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
public class StatisticController {

    private final StatisticService statisticSerivice;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/stats")
    public ResponseEntity<StatisticResponse> getRestaurantStatistic(@PathVariable UUID restaurantId,
                                                                    @ModelAttribute StatisticFilter filters) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(statisticSerivice.getRestaurantStatistic(restaurantId, filters), HttpStatus.OK);
    }
}
