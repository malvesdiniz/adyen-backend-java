package com.mosselsouper.adyenbackend.controller;

import com.mosselsouper.adyenbackend.model.PaymentRequest;
import com.mosselsouper.adyenbackend.service.AdyenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    private final AdyenService adyenService;

    public PaymentController(AdyenService adyenService) {
        this.adyenService = adyenService;
    }

    @PostMapping("/payment-session")
    public ResponseEntity<String> createPaymentSession(@RequestBody PaymentRequest request) {
        String response = adyenService.createPaymentSession(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/payment-submit")
    public String submitPayment(@RequestBody Map<String, Object> paymentData) {
        return adyenService.submitPayment(paymentData);
    }
    @PostMapping("/payment-details")
    public String submitPaymentDetails(@RequestBody Map<String, Object> detailsData) {
        return adyenService.submitPaymentDetails(detailsData);
    }
}
