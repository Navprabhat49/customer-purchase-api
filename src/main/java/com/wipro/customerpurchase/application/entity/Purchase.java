package com.wipro.customerpurchase.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private LocalDateTime createdAt;

}
