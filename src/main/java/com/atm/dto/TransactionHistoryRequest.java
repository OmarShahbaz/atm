package com.atm.dto;

import lombok.Data;

@Data
public class TransactionHistoryRequest {

    private String accountType;

    private String transactionType;

    private String accountNumber;

}
