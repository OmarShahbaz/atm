package com.atm.service;

import com.atm.dto.AccountRequest;
import com.atm.dto.AccountResponse;
import com.atm.dto.AccountUpdateRequest;
import com.atm.dto.AccountUpdateResponse;
import org.hibernate.sql.Update;

public interface AccountService {

    //create account
    AccountResponse createAccount(AccountRequest accountRequest, String email);

    //update account details
    AccountUpdateResponse updateAccount(AccountUpdateRequest updateRequest, String email);
}
