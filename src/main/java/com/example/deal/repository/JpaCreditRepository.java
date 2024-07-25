package com.example.deal.repository;

import com.example.deal.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCreditRepository extends JpaRepository<Credit, Long> {
}
