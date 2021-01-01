package com.przemarcz.order.controller;

import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.przemarcz.order.dto.PaymentDto.CreatePaymentRequest;
import static com.przemarcz.order.dto.PaymentDto.PaymentResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @PostMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentResponse> addPayment(@PathVariable UUID restaurantId, @RequestBody CreatePaymentRequest createPaymentRequest) {
        return new ResponseEntity<>(paymentService.addPayment(restaurantId,createPaymentRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('OWNER_'+#restaurantId)")
    @DeleteMapping("/{restaurantId}/payment")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable UUID restaurantId) {
        paymentService.deletePayment(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
