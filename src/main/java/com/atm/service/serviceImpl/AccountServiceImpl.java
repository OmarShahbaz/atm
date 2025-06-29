package com.atm.service.serviceImpl;

import com.atm.common.AccountType;
import com.atm.dto.AccountRequest;
import com.atm.dto.AccountResponse;
import com.atm.dto.AccountUpdateRequest;
import com.atm.dto.AccountUpdateResponse;
import com.atm.exception.DuplicateAccountTypeException;
import com.atm.exception.InvalidAccountTypeException;
import com.atm.exception.UserNotFoundException;
import com.atm.model.Account;
import com.atm.model.User;
import com.atm.repository.AccountRepository;
import com.atm.repository.UserRepository;
import com.atm.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest, String email) {
        Account account = new Account();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist", "email", email));
        try{
            account.setAccountType(AccountType.valueOf(accountRequest.getAccountType().toUpperCase()));
        } catch (IllegalArgumentException ex){
            throw new InvalidAccountTypeException("Invalid Account Type", "accountType", accountRequest.getAccountType());
        }

        Optional<Account> existingAccount = accountRepository.findByUserAndAccountType(user, account.getAccountType());
        if(existingAccount.isPresent()){
            throw new DuplicateAccountTypeException("Only one account of same type is allowed", "accountType", account.getAccountType().toString());
        }

        account.setAccountHolderName(user.getUsername());
        account.setAccountNumber(generateUniqueAccountNumber());
        log.info("Balance to check {}",accountRequest.getInitialDeposit());
        account.setInitialDeposit(accountRequest.getInitialDeposit());
        account.setUser(user);
        accountRepository.save(account);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountHolder(account.getAccountHolderName());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setBalance(account.getInitialDeposit());
        return accountResponse;
    }

    @Override
    public AccountUpdateResponse updateAccount(AccountUpdateRequest updateRequest, String email) {
        Account account = new Account();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist", "email", email));
        List<Account> accounts = accountRepository.findByUser(user);
        List<Account> list = accounts.stream()
                .peek(account1 -> account1.setAccountHolderName(updateRequest.getAccountHolderName()))
                .toList();
        accountRepository.saveAll(list);
        log.info(accounts.toString());
        user.setUsername(updateRequest.getAccountHolderName());
        userRepository.save(user);
        AccountUpdateResponse updateResponse = new AccountUpdateResponse();
        updateResponse.setAccountHolderName(updateRequest.getAccountHolderName());
        return updateResponse;
    }

    public String generateAccountNumber(){
        long accountNumber = (long) (Math.random() * 1_000_000_000L);
        return String.format("%010d", accountNumber);
    }

    public String generateUniqueAccountNumber(){
        String accNum;
        do{
            accNum = generateAccountNumber();
        } while(accountRepository.existsByAccountNumber(accNum));
        return accNum;
    }

}