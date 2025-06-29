package com.atm.service;

import com.atm.dto.*;


import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    TransactionResponse withdraw(TransactionRequest transactionRequest, String email);

    TransactionResponse deposit(TransactionRequest transactionRequest, String email);

    List<TransactionHistoryResponse> history(TransactionHistoryRequest transactionHistoryRequest, String email);

    List<TransactionHistoryResponse> historyByType(TransactionHistoryRequest transactionHistoryRequest, String email);

    BigDecimal checkUserBalance(CheckBalanceRequest balanceRequest);


}
