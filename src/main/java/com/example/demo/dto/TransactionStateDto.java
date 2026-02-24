package com.example.demo.dto;

import java.time.Instant;

/**
 * DTO returned to clients representing the latest or historical state of a transaction.
 */
public class TransactionStateDto {

    private Long id;
    private String transactionId;
    private String serviceName;
    private String user;
    private Instant timestamp;
    // human-readable state name and description resolved from the lookup table
    private String stateName;
    private String stateDescription;

    public TransactionStateDto() {
    }

    public TransactionStateDto(Long id, String transactionId, String state, String serviceName, String user, Instant timestamp) {
        this.id = id;
        this.transactionId = transactionId;
        this.serviceName = serviceName;
        this.user = user;
        this.timestamp = timestamp;
    }

    // New getters/setters for lookup-resolved fields
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
