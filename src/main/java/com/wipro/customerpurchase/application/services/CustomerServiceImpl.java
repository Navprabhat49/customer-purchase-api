package com.wipro.customerpurchase.application.services;

import com.wipro.customerpurchase.adapter.out.adapter.CustomerRepository;
import com.wipro.customerpurchase.application.entity.Purchase;
import com.wipro.customerpurchase.application.port.CustomerService;
import com.wipro.customerpurchase.domain.model.CustomerPurchase;
import com.wipro.customerpurchase.domain.model.PurchaseRequest;
import com.wipro.customerpurchase.domain.model.PurchaseResponse;
import com.wipro.customerpurchase.domain.model.StatusAndMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    public static final String CUSTOMER_PURCHASE_API = "customer-purchase-api";
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void printEnv() {
        LOGGER.info(CUSTOMER_PURCHASE_API + " database url - {}", dbUrl);
        LOGGER.info(CUSTOMER_PURCHASE_API + " database username - {}", username);
        LOGGER.info(CUSTOMER_PURCHASE_API + " database password - {}", password);
    }

    @Override
    public StatusAndMessage createCustomerPurchase(PurchaseRequest request) {
        Purchase purchase = mapPurchaseRequest(request);
        Purchase saved = customerRepository.save(purchase);
        StatusAndMessage statusAndMessage;
        if(saved.getId()!=null){
            LOGGER.info(CUSTOMER_PURCHASE_API + " insert is success ");
            statusAndMessage = new StatusAndMessage(0, "Inserted Successfully");
        } else {
            LOGGER.error(CUSTOMER_PURCHASE_API + "Failed to insert");
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
            LOGGER.error(CUSTOMER_PURCHASE_API + " Both startDate and endDate are required");
            throw new IllegalArgumentException("Both startDate and endDate are required");
        }

        if(customerName!=null && !customerName.isEmpty()){
            List<Purchase> purchaseListBasedOnCustomer = customerRepository.findByCustomerNameIgnoreCase(
                    customerName);
            mapPurchaseList(purchaseListBasedOnCustomer, customerPurchaseList);
            return new PurchaseResponse(2, customerPurchaseList);
        }

        if(customerName != null){
            LOGGER.error(CUSTOMER_PURCHASE_API + " Customer name is required");
            throw new IllegalArgumentException("Customer name is required");
        }

        List<Purchase> purchaseList = customerRepository.findAll();
        mapPurchaseList(purchaseList, customerPurchaseList);
        LOGGER.info(CUSTOMER_PURCHASE_API + " response list size - {} ", customerPurchaseList.size());
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
