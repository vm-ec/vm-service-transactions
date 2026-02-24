package com.example.demo.entity;

import jakarta.persistence.*;


/**
 * Lookup table for transaction states (6 fixed rows)
 */
@Entity
@Table(name = "transaction_state_lookup")
public class TransactionStateLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state_name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    public TransactionStateLookup() {
    }

    public TransactionStateLookup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

