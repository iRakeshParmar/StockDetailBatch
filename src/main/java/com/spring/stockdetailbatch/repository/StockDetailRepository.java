package com.spring.stockdetailbatch.repository;

import com.spring.stockdetailbatch.entity.JPA.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
}