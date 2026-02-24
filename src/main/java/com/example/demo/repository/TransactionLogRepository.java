package com.example.demo.repository;

import com.example.demo.entity.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository with convenience queries used by the dashboard APIs.
 */
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    // latest state for a given transactionId
    Optional<TransactionLog> findTopByTransactionIdOrderByTimestampDesc(String transactionId);

    // history for a given transactionId
    Page<TransactionLog> findAllByTransactionIdOrderByTimestampDesc(String transactionId, Pageable pageable);

    // all failed
    Page<TransactionLog> findAllByStateOrderByTimestampDesc(String state, Pageable pageable);

    // failed by service
    Page<TransactionLog> findAllByStateAndServiceNameOrderByTimestampDesc(String state, String serviceName, Pageable pageable);

    // allow a lightweight count to back metrics (optional)
    @Query("SELECT COUNT(t) FROM TransactionLog t WHERE t.state = :state")
    long countByState(@Param("state") String state);
}

