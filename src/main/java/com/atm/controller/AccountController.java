package com.atm.controller;

import com.atm.dto.AccountRequest;
import com.atm.dto.AccountResponse;
import com.atm.dto.AccountUpdateRequest;
import com.atm.dto.AccountUpdateResponse;
import com.atm.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2")
public class AccountController {

    private final AccountService accountService;

    public AccountController (AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(accountService.createAccount(accountRequest, accountRequest.getAccountEmail()), HttpStatus.CREATED);
    }

    @PatchMapping("/update-account")
    public ResponseEntity<AccountUpdateResponse> updateAccount(@Valid @RequestBody AccountUpdateRequest updateRequest){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        System.out.println("Logged-in user: " + authentication.getName());
        String email = authentication.getName();
        return new ResponseEntity<>(accountService.updateAccount(updateRequest, updateRequest.getAccountEmail()), HttpStatus.OK);
    }

    //delete account (means inactive account)

    //admin want to check all his accounts affiliated with us, retrieve accounts

}
