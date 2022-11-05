package com.bankin.app.config;

import lombok.Data;

@Data
public class CreditConfig {
    private double interestRate;
    private double lateFee;
    private double annualFee;
    private String paymentDueDate;
    private String limit;
}
