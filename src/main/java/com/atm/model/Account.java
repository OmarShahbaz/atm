package com.atm.model;

import com.atm.common.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id",
                "account_type"
        })})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(name = "holderName")
    private String accountHolderName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal initialDeposit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistory = new ArrayList<>();
}