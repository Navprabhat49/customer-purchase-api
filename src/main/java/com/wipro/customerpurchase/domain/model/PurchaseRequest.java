package com.wipro.customerpurchase.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {

    private String customerName;
    private String product;
    private int quantity;
    private double price;
}
