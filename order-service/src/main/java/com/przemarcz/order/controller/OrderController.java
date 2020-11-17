package com.przemarcz.order.controller;

import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('OWNER_'+#restaurantId,'WORKER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PathVariable UUID restaurantId,
                                                       Pageable pageable) {
        return new ResponseEntity<>(orderService.getAllOrders(restaurantId, pageable), HttpStatus.OK);
    }
}
