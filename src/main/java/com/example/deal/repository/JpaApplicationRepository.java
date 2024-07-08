package com.example.deal.repository;

import com.example.deal.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaApplicationRepository extends JpaRepository<Client, Long> {
}
