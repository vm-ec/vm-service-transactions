package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * Entity representing a single state-change event for a transaction.
 * Each time a transaction moves to a new state a new TransactionLog row is written.
 * Fields are intentionally minimal to reflect the requirement: [id, transaction_id, state, timestamp, user].
 */
@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "state", nullable = false)
    private String state;

    // optional field indicating which service produced this log (helps filter failed-by-service)
    @Column(name = "service_name")
    private String serviceName;

    // user who triggered the state change (may be system)
    @Column(name = "username")
    private String user;

    // timestamp of the event
    @Column(name = "event_time", nullable = false)
    private Instant timestamp;

    public TransactionLog() {
        // JPA requires a no-arg constructor
    }

    public TransactionLog(String transactionId, String state, String serviceName, String user, Instant timestamp) {
        this.transactionId = transactionId;
        this.state = state;
        this.serviceName = serviceName;
        this.user = user;
        this.timestamp = timestamp;
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

