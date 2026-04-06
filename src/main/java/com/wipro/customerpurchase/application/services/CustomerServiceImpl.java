package com.wipro.customerpurchase.application.services;

import com.wipro.customerpurchase.adapter.out.adapter.CustomerRepository;
import com.wipro.customerpurchase.application.entity.Purchase;
import com.wipro.customerpurchase.application.port.CustomerService;
import com.wipro.customerpurchase.domain.model.CustomerPurchase;
import com.wipro.customerpurchase.domain.model.PurchaseRequest;
import com.wipro.customerpurchase.domain.model.PurchaseResponse;
import com.wipro.customerpurchase.domain.model.StatusAndMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public StatusAndMessage createCustomerPurchase(PurchaseRequest request) {
        Purchase purchase = mapPurchaseRequest(request);
        Purchase saved = customerRepository.save(purchase);
        StatusAndMessage statusAndMessage;
        if(saved.getId()!=null){
            statusAndMessage = new StatusAndMessage(0, "Inserted Successfully");
        } else {
            statusAndMessage = new StatusAndMessage(1, "Failed to insert");
        }
        return statusAndMessage;
    }

    @Override
    public PurchaseResponse getAllPurchases(String startDate, String endDate, String customerName) {
        List<CustomerPurchase> customerPurchaseList = new ArrayList<>();
        if(startDate!=null && endDate!=null){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);
            List<Purchase> purchaseListBetweenDates = customerRepository.findByCreatedAtBetween(
                    startDateTime, endDateTime);
            mapPurchaseList(purchaseListBetweenDates, customerPurchaseList);
            return new PurchaseResponse(2, customerPurchaseList);
        }
        if(startDate!=null || endDate!=null){
            throw new IllegalArgumentException("Both startDate and endDate are required");
        }

        if(customerName!=null && !customerName.isEmpty()){
            List<Purchase> purchaseListBasedOnCustomer = customerRepository.findByCustomerNameIgnoreCase(
                    customerName);
            mapPurchaseList(purchaseListBasedOnCustomer, customerPurchaseList);
            return new PurchaseResponse(2, customerPurchaseList);
        }

        if(customerName != null){
            throw new IllegalArgumentException("Customer name is required");
        }

        List<Purchase> purchaseList = customerRepository.findAll();
        mapPurchaseList(purchaseList, customerPurchaseList);
        return new PurchaseResponse(2, customerPurchaseList);
    }

    private void mapPurchaseList(List<Purchase> purchaseList, List<CustomerPurchase> customerPurchaseList) {
        for(Purchase purchase: purchaseList){
            CustomerPurchase cp = CustomerPurchase.builder().id(purchase.getId())
                    .customerName(purchase.getCustomerName()).product(purchase.getProduct())
                    .quantity(purchase.getQuantity()).price(Double.parseDouble(String.valueOf(purchase.getPrice())))
                    .amount(Double.parseDouble(String.valueOf(purchase.getAmount())))
                    .createdAt(purchase.getCreatedAt()).build();
            customerPurchaseList.add(cp);
        }
    }

    private Purchase mapPurchaseRequest(PurchaseRequest request) {
        Purchase purchase = new Purchase();
        purchase.setProduct(request.getProduct());
        purchase.setPrice(BigDecimal.valueOf(request.getPrice()));
        purchase.setQuantity(request.getQuantity());
        purchase.setCustomerName(request.getCustomerName());
        purchase.setAmount(BigDecimal.valueOf(request.getPrice() * request.getQuantity()));
        purchase.setCreatedAt(LocalDateTime.now());
        return purchase;
    }
}
