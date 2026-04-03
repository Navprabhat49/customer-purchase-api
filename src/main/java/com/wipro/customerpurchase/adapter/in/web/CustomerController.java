package com.wipro.customerpurchase.adapter.in.web;

import com.wipro.customerpurchase.application.port.CustomerService;
import com.wipro.customerpurchase.domain.model.PurchaseRequest;
import com.wipro.customerpurchase.domain.model.PurchaseResponse;
import com.wipro.customerpurchase.domain.model.StatusAndMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class CustomerController {


    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<StatusAndMessage> createCustomerPurchase(@RequestBody PurchaseRequest request){
        StatusAndMessage result = customerService.createCustomerPurchase(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/purchase")
    public ResponseEntity<PurchaseResponse> getAllPurchaseResponse(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String customerName){
        PurchaseResponse result = customerService.getAllPurchases(startDate, endDate, customerName);
        return ResponseEntity.ok(result);
    }
}
