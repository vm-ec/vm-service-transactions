package com.example.demo.repository;

import com.example.demo.entity.TransactionStateLookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionStateLookupRepository extends JpaRepository<TransactionStateLookup, Long> {
    Optional<TransactionStateLookup> findByName(String name);
}

