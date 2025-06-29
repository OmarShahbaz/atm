package com.atm.model;


import com.atm.common.AccountType;
import com.atm.common.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactionHistories")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transaction_id;

    @Column(name = "reason")
    private String description;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String accountNumber;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal previousBalance;

    private BigDecimal newBalance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
