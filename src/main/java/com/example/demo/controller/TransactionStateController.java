package com.example.demo.controller;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.TransactionStateDto;
import com.example.demo.service.TransactionStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller exposing the operational dashboard APIs for transaction state.
 * - GET /api/transactions (findAll)
 * - GET /api/transactions/{transactionId}/state (latest state)
 * - GET /api/transactions/{transactionId} (history)
 * - GET /api/transactions/failed (failed list)
 * - GET /api/transactions/failed/service/{serviceName} (failed by service)
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionStateController {

    private final TransactionStateService service;

    @Autowired
    public TransactionStateController(TransactionStateService service) {
        this.service = service;
    }

    private Pageable toPageable(Integer page, Integer size, String sort) {
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 50 : Math.min(size, 1000);
        if (sort == null) {
            return PageRequest.of(p, s, Sort.by(Sort.Direction.DESC, "timestamp"));
        }
        String[] parts = sort.split(",");
        if (parts.length == 2) {
            Sort.Direction dir = parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            return PageRequest.of(p, s, Sort.by(dir, parts[0]));
        }
        return PageRequest.of(p, s);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionStateDto>> findAll(@RequestParam(required = false) Integer page,
                                                                        @RequestParam(required = false) Integer size,
                                                                        @RequestParam(required = false) String sort) {
        Pageable pageable = toPageable(page, size, sort);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{transactionId}/state")
    public ResponseEntity<?> getLatestState(@PathVariable String transactionId) {
        Optional<TransactionStateDto> dto = service.getLatestState(transactionId);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        } else {
            // return a consistent JSON-like error object
            return ResponseEntity.status(404).body(java.util.Map.of("error", "NOT_FOUND", "message", "transactionId not found"));
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PagedResponse<TransactionStateDto>> findAllByTransactionId(@PathVariable String transactionId,
                                                                                    @RequestParam(required = false) Integer page,
                                                                                    @RequestParam(required = false) Integer size,
                                                                                    @RequestParam(required = false) String sort) {
        Pageable pageable = toPageable(page, size, sort);
        return ResponseEntity.ok(service.findAllByTransactionId(transactionId, pageable));
    }

    @GetMapping("/failed")
    public ResponseEntity<PagedResponse<TransactionStateDto>> findAllFailed(@RequestParam(required = false) Integer page,
                                                                             @RequestParam(required = false) Integer size,
                                                                             @RequestParam(required = false) String sort) {
        Pageable pageable = toPageable(page, size, sort);
        return ResponseEntity.ok(service.findAllFailed(pageable));
    }

    @GetMapping("/failed/service/{serviceName}")
    public ResponseEntity<PagedResponse<TransactionStateDto>> findAllFailedByService(@PathVariable String serviceName,
                                                                                     @RequestParam(required = false) Integer page,
                                                                                     @RequestParam(required = false) Integer size,
                                                                                     @RequestParam(required = false) String sort) {
        Pageable pageable = toPageable(page, size, sort);
        return ResponseEntity.ok(service.findAllFailedByService(serviceName, pageable));
    }

    @PostMapping
    public ResponseEntity<TransactionStateDto> createTransactionLog(@RequestBody com.example.demo.dto.TransactionLogCreateRequest request) {
        TransactionStateDto created = service.createTransactionLog(request);
        return ResponseEntity.status(201).body(created);
    }
}
