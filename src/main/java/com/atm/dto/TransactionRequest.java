package com.atm.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotBlank(message = "state purpose of this transaction")
    private String description;

    @NotBlank(message = "account type is required")
    private String accountType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Withdrawal/Deposit amount must be greater than zero")
    private BigDecimal amount;
}
