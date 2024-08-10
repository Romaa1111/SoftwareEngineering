package com.example.demodemo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String cname;
    private String email;
    private BigDecimal initialDeposit;
    private int numberOfYears;
    private String savingsType;

    // Default constructor
    public Customer() {
    }

    // Parameterized constructor
    public Customer(int id, String cname, String email, BigDecimal initialDeposit, int numberOfYears, String savingsType) {
        this.id = id;
        this.cname = cname;
        this.email = email;
        this.initialDeposit = initialDeposit;
        this.numberOfYears = numberOfYears;
        this.savingsType = savingsType;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public String getSavingsType() {
        return savingsType;
    }

    public void setSavingsType(String savingsType) {
        this.savingsType = savingsType;
    }
}
