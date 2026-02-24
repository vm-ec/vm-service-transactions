package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

/**
 * Request body for creating a new TransactionLog entry.
 */
public class TransactionLogCreateRequest {

    private String transactionId;
    private String state;
    private String serviceName;
    private String user;

    // optional timestamp; if not provided, server will set Instant.now()
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    public TransactionLogCreateRequest() {
    }

    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

