package com.example.demo.service;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.TransactionStateDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransactionStateService {

    Optional<TransactionStateDto> getLatestState(String transactionId);

    PagedResponse<TransactionStateDto> findAll(Pageable pageable);

    PagedResponse<TransactionStateDto> findAllByTransactionId(String transactionId, Pageable pageable);

    PagedResponse<TransactionStateDto> findAllFailed(Pageable pageable);

    PagedResponse<TransactionStateDto> findAllFailedByService(String serviceName, Pageable pageable);

    // create a new TransactionLog entry and return the DTO of created entity
    TransactionStateDto createTransactionLog(com.example.demo.dto.TransactionLogCreateRequest request);
}
