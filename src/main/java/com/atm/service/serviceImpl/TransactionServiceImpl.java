package com.atm.service.serviceImpl;

import com.atm.common.AccountType;
import com.atm.common.TransactionType;
import com.atm.dto.*;
import com.atm.exception.AccountNotFoundException;
import com.atm.exception.AmountLessThenZeroException;
import com.atm.exception.UserNotFoundException;
import com.atm.model.Account;
import com.atm.model.TransactionHistory;
import com.atm.model.User;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;
import com.atm.repository.UserRepository;
import com.atm.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(UserRepository userRepository, AccountRepository accountRepository,  TransactionRepository transactionRepository){
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest transactionRequest, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist", "email", email));
        Account account = accountRepository.findByUserAndAccountType(user, AccountType.valueOf(transactionRequest.getAccountType().toUpperCase()))
                .orElseThrow(()->
                        new AccountNotFoundException("Withdraw not possible, account does not exist", "email",user.getEmail()));

        BigDecimal newAmount = afterWithdrawBalance(account.getInitialDeposit(), transactionRequest.getAmount());

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setAccountNumber(account.getAccountNumber());
        transactionHistory.setDescription(transactionRequest.getDescription());
        transactionHistory.setTransactionType(TransactionType.WITHDRAW);
        transactionHistory.setPreviousBalance(account.getInitialDeposit());
        transactionHistory.setNewBalance(newAmount);
        transactionHistory.setDateTime(LocalDateTime.now());
        transactionHistory.setAccountType(account.getAccountType());
        transactionHistory.setAccount(account);
        transactionRepository.save(transactionHistory);

        account.setInitialDeposit(newAmount);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setAccountType(transactionRequest.getAccountType());
        transactionResponse.setCnic(user.getNic());
        transactionResponse.setAccountHolderName(account.getAccountHolderName());
        transactionResponse.setAccountNumber(account.getAccountNumber());
        transactionResponse.setNewBalance(newAmount);

        accountRepository.save(account);
        return transactionResponse;
    }


    @Override
    public TransactionResponse deposit(TransactionRequest transactionRequest, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist", "email", email));
        Account account = accountRepository.findByUserAndAccountType(user, AccountType.valueOf(transactionRequest.getAccountType().toUpperCase()))
                .orElseThrow(() -> new AccountNotFoundException("Deposit not possible, account does not exist", "email", user.getEmail()));

        BigDecimal amount = afterDepositBalance(account.getInitialDeposit(), transactionRequest.getAmount());

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setAccountNumber(account.getAccountNumber());
        transactionHistory.setDescription(transactionRequest.getDescription());
        transactionHistory.setTransactionType(TransactionType.DEPOSIT);
        transactionHistory.setPreviousBalance(account.getInitialDeposit());
        transactionHistory.setNewBalance(amount);
        transactionHistory.setDateTime(LocalDateTime.now());
        transactionHistory.setAccount(account);
        transactionHistory.setAccountType(account.getAccountType());

        account.setInitialDeposit(amount);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCnic(user.getNic());
        transactionResponse.setAccountType(transactionRequest.getAccountType());
        transactionResponse.setNewBalance(amount);
        transactionResponse.setAccountHolderName(user.getUsername());
        transactionResponse.setAccountNumber(account.getAccountNumber());

        transactionRepository.save(transactionHistory);
        accountRepository.save(account);
        return transactionResponse;
    }

    @Override
    public List<TransactionHistoryResponse> history(TransactionHistoryRequest transactionHistoryRequest, String email) {
        AccountType accountType = AccountType.valueOf(transactionHistoryRequest.getAccountType().toUpperCase());
        List<TransactionHistory> th = transactionRepository.findByAccount_User_EmailAndAccountType(email, accountType);
        List<TransactionHistoryResponse> historyResponse = new ArrayList<>();
        List<TransactionHistoryResponse> result = th.stream()
                .map(history -> new TransactionHistoryResponse(
                        history.getTransaction_id(),
                        history.getAccountType(),
                        history.getAccountNumber(),
                        history.getDateTime(),
                        history.getTransactionType().toString(),
                        history.getPreviousBalance(),
                        history.getNewBalance()
                        ))
                .toList();
        return result;
    }

    @Override
    public List<TransactionHistoryResponse> historyByType(TransactionHistoryRequest transactionHistoryRequest, String email) {
        TransactionType transactionType = TransactionType.valueOf(transactionHistoryRequest.getTransactionType().toUpperCase());
        AccountType accountType = AccountType.valueOf(transactionHistoryRequest.getAccountType().toUpperCase());
        List<TransactionHistory> th = transactionRepository.findByAccount_User_EmailAndAccountTypeAndTransactionType(email, accountType, transactionType);
        List<TransactionHistoryResponse> histories = th.stream().map(history ->
                new TransactionHistoryResponse(
                        history.getTransaction_id(),
                        history.getAccountType(),
                        history.getAccountNumber(),
                        history.getDateTime(),
                        history.getTransactionType().toString(),
                        history.getPreviousBalance(),
                        history.getNewBalance()
                )).toList();
        return histories;
    }

    public BigDecimal checkUserBalance(CheckBalanceRequest balanceRequest){
        List<Account> accounts = accountRepository.findByAccountHolderName(balanceRequest.getHolderName());
        Account filteredAccount = accounts.stream()
                .filter(account -> account.getAccountType() == AccountType.valueOf(balanceRequest.getAccountType().toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist","account type", balanceRequest.getAccountType()));
        return filteredAccount.getInitialDeposit();
    }

    public BigDecimal afterWithdrawBalance(BigDecimal previousAmount, BigDecimal withdrawAmount) {
        if(previousAmount.compareTo(withdrawAmount)>=0){
            return previousAmount.subtract(withdrawAmount);
        } else
            throw new AmountLessThenZeroException("withdraw amount is more then your current balance", "current balance", previousAmount);
    }

    public BigDecimal afterDepositBalance(BigDecimal previousAmount, BigDecimal newAmount) {
        return previousAmount.add(newAmount);
    }
}
