package com.atm.controller;

import com.atm.dto.*;
import com.atm.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v3")
@Slf4j
public class TransactionController {


    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return new ResponseEntity<>(transactionService.withdraw(transactionRequest, email), HttpStatus.OK);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest transactionRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return new ResponseEntity<>(transactionService.deposit(transactionRequest, email), HttpStatus.OK);
    }

    @GetMapping("history")
    public ResponseEntity<List<TransactionHistoryResponse>> history(@RequestBody TransactionHistoryRequest transactionHistoryRequest){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        return new ResponseEntity<>(transactionService.history(transactionHistoryRequest, email), HttpStatus.OK);
    }

    @GetMapping("history/byType")
    public ResponseEntity<List<TransactionHistoryResponse>> historyByType(@RequestBody TransactionHistoryRequest transactionHistoryRequest){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(transactionService.historyByType(transactionHistoryRequest, email), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<BigDecimal> checkBalance(@Valid @RequestBody CheckBalanceRequest checkBalanceRequest){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        System.out.println("Logged-in user: " + authentication.getName());
        return new ResponseEntity<>(transactionService.checkUserBalance(checkBalanceRequest), HttpStatus.OK);
    }

}
