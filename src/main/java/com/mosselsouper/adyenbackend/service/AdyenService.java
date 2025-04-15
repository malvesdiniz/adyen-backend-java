package com.mosselsouper.adyenbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosselsouper.adyenbackend.model.PaymentRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdyenService {

    private static final String API_KEY = "AQEvhmfxKYrOaRVCw0m/n3Q5qf3VeLNhLJBJV3BY0inyz4dAmgRERhoOHn0DvOtlSPcQwV1bDb7kfNy1WIxIIkxgBw==-2Logm7HCiKKzPCAKlOp+yCdZgiDx2xoT8k5LKVTtqPU=-i1itGb@U;3Nk4zf9K$@";
    private static final String MERCHANT_ACCOUNT = "PXLAccount191ECOM";
    private static final String ADYEN_URL = "https://checkout-test.adyen.com/v71/sessions";

    public String createPaymentSession(PaymentRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-API-Key", API_KEY);

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency", request.getCurrency());
        amount.put("value", request.getAmount());

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("reference", request.getReference());
        body.put("merchantAccount", MERCHANT_ACCOUNT);
        body.put("returnUrl", "http://localhost:4200/success");
        body.put("countryCode", "BE");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                ADYEN_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
    public String submitPayment(Map<String, Object> paymentData) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-API-Key", API_KEY);

        Map<String, Object> body = new HashMap<>(paymentData);

        body.putIfAbsent("merchantAccount", MERCHANT_ACCOUNT);
        body.putIfAbsent("reference", "order-12345");

        if (!body.containsKey("amount")) {
            Map<String, Object> amount = new HashMap<>();
            amount.put("currency", "EUR");
            amount.put("value", 1000);
            body.put("amount", amount);
        }

        body.putIfAbsent("origin", "http://localhost:4200");
        if (!body.containsKey("browserInfo")) {
            Map<String, String> browserInfo = new HashMap<>();
            browserInfo.put("userAgent", "Mozilla/5.0 ...");
            browserInfo.put("acceptHeader", "*/*");
            browserInfo.put("language", "pt-BR");
            browserInfo.put("colorDepth", "24");
            browserInfo.put("screenHeight", "1080");
            browserInfo.put("screenWidth", "1920");
            browserInfo.put("timeZoneOffset", "0");
            browserInfo.put("javaEnabled", "false");
            browserInfo.put("javaScriptEnabled", "true");
            body.put("browserInfo", browserInfo);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            System.out.println("Sending to Adyen:\n" + json);
        } catch (Exception e) {
            System.out.println("Error serializing request body");
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://checkout-test.adyen.com/v71/payments",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    public String submitPaymentDetails(Map<String, Object> detailsData) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-API-Key", API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(detailsData, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://checkout-test.adyen.com/v71/payments/details",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
