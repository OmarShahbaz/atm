package com.atm.dto;

import com.atm.common.AccountType;
import com.atm.common.TransactionType;
import com.atm.model.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {

    private int id;

    private AccountType accountType;

    private String accountNumber;

    private LocalDateTime dateTime;

    private String transactionType;

    private BigDecimal previousBalance;

    private BigDecimal newBalance;


}
