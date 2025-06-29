package com.atm.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank(message = "email is required")
    private String accountEmail;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "100.00", message = "Initial deposit must be at least 100.00")
    private BigDecimal initialDeposit;
}
