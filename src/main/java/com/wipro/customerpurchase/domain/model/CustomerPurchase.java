package com.wipro.customerpurchase.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CustomerPurchase {

    private Long id;
    private String customerName;
    private String product;
    private Integer quantity;
    private Double price;
    private Double amount;
    private LocalDateTime createdAt;

}
