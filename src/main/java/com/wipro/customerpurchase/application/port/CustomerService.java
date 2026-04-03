package com.wipro.customerpurchase.application.port;

import com.wipro.customerpurchase.domain.model.PurchaseRequest;
import com.wipro.customerpurchase.domain.model.PurchaseResponse;
import com.wipro.customerpurchase.domain.model.StatusAndMessage;

public interface CustomerService {
    StatusAndMessage createCustomerPurchase(PurchaseRequest request);

    PurchaseResponse getAllPurchases(String startDate, String endDate, String customerName);
}
