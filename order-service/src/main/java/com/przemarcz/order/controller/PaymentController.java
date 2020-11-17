package com.przemarcz.order.controller;

import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.service.PaymentService;
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
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentDto> addPayment(@PathVariable UUID restaurantId, @RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.addPayment(restaurantId,paymentDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @GetMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID restaurantId) {
        return new ResponseEntity<>(paymentService.getPayment(restaurantId),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PutMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable UUID restaurantId, @RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.updatePayment(restaurantId,paymentDto),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable UUID restaurantId) {
        paymentService.deletePayment(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
