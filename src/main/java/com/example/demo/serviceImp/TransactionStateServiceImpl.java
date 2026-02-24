package com.example.demo.serviceImp;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.TransactionStateDto;
import com.example.demo.entity.TransactionLog;
import com.example.demo.repository.TransactionLogRepository;
import com.example.demo.repository.TransactionStateLookupRepository;
import com.example.demo.service.TransactionStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionStateServiceImpl implements TransactionStateService {

    private final TransactionLogRepository repository;
    private final TransactionStateLookupRepository lookupRepository;

    @Autowired
    public TransactionStateServiceImpl(TransactionLogRepository repository, TransactionStateLookupRepository lookupRepository) {
        this.repository = repository;
        this.lookupRepository = lookupRepository;
    }

    private TransactionStateDto toDto(TransactionLog e) {
        TransactionStateDto dto = new TransactionStateDto(e.getId(), e.getTransactionId(), e.getState(), e.getServiceName(), e.getUser(), e.getTimestamp());
        // resolve lookup values if present
        lookupRepository.findByName(e.getState()).ifPresentOrElse(l -> {
            dto.setStateName(l.getName());
            dto.setStateDescription(l.getDescription());
        }, () -> {
            // fallback: use raw state code as a human-friendly name when lookup is missing
            dto.setStateName(e.getState());
            dto.setStateDescription(null);
        });
        return dto;
    }

    @Override
    public Optional<TransactionStateDto> getLatestState(String transactionId) {
        return repository.findTopByTransactionIdOrderByTimestampDesc(transactionId).map(this::toDto);
    }

    @Override
    public PagedResponse<TransactionStateDto> findAll(Pageable pageable) {
        Page<TransactionLog> page = repository.findAll(pageable);
        List<TransactionStateDto> content = page.stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public PagedResponse<TransactionStateDto> findAllByTransactionId(String transactionId, Pageable pageable) {
        Page<TransactionLog> page = repository.findAllByTransactionIdOrderByTimestampDesc(transactionId, pageable);
        List<TransactionStateDto> content = page.stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public PagedResponse<TransactionStateDto> findAllFailed(Pageable pageable) {
        Page<TransactionLog> page = repository.findAllByStateOrderByTimestampDesc("FAILED", pageable);
        List<TransactionStateDto> content = page.stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public PagedResponse<TransactionStateDto> findAllFailedByService(String serviceName, Pageable pageable) {
        Page<TransactionLog> page = repository.findAllByStateAndServiceNameOrderByTimestampDesc("FAILED", serviceName, pageable);
        List<TransactionStateDto> content = page.stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public TransactionStateDto createTransactionLog(com.example.demo.dto.TransactionLogCreateRequest request) {
        java.time.Instant ts = request.getTimestamp() == null ? java.time.Instant.now() : request.getTimestamp();
        TransactionLog log = new TransactionLog(request.getTransactionId(), request.getState(), request.getServiceName(), request.getUser(), ts);
        TransactionLog saved = repository.save(log);
        return toDto(saved);
    }
}
