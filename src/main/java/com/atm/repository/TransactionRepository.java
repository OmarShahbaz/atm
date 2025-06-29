package com.atm.repository;

import com.atm.common.AccountType;
import com.atm.common.TransactionType;
import com.atm.model.Account;
import com.atm.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer> {

//    List<TransactionHistory> findByAccountNumber(String accountNumber);

    Account findByAccount_User_Email(String email);

    List<TransactionHistory> findByAccount_User_EmailAndAccountTypeAndTransactionType(String email,AccountType accountType, TransactionType transactionType);

    List<TransactionHistory> findByAccount_User_EmailAndAccountType(String email, AccountType accountType);
}
