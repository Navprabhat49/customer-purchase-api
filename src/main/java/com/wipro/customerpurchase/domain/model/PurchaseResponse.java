package com.wipro.customerpurchase.domain.model;

import java.util.List;

public record PurchaseResponse(int status, List<CustomerPurchase> customerPurchaseList) {
}
