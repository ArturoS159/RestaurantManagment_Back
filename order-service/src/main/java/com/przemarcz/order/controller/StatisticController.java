package com.przemarcz.order.controller;

import com.przemarcz.order.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.przemarcz.order.dto.StatisticDto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
public class StatisticController {

    private final StatisticService statisticSerivice;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/stats-owner")
    public ResponseEntity<Void> getRestaurantStatistic(@PathVariable UUID restaurantId,
                                                               @ModelAttribute StatisticFilter filters) {
        statisticSerivice.getRestaurantStatistic(restaurantId, filters);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
