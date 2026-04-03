package com.wipro.customerpurchase.adapter.out.adapter;

import com.wipro.customerpurchase.application.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Purchase> findByCustomerNameIgnoreCase(String customerName);
}
