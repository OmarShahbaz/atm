package com.atm.repository;

import com.atm.common.AccountType;
import com.atm.model.Account;
import com.atm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByUserAndAccountType(User user, AccountType accountType);

    List<Account> findByUser(User user);

    List<Account> findByAccountHolderName(String holderName);

}
