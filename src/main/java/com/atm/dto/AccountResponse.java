package com.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    private String accountHolder;

    private String accountNumber;

    private BigDecimal balance;
}
