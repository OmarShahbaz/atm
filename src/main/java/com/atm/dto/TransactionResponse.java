package com.atm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionResponse {

    private String accountType;

    private String accountHolderName;

    private String accountNumber;

    private String cnic;

    private BigDecimal newBalance;
}
