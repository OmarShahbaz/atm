package com.atm.service;

import com.atm.dto.LoginRequest;
import com.atm.dto.LoginResponse;
import com.atm.dto.UserSignupRequest;
import com.atm.dto.UserSignupResponse;

public interface UserService {

    UserSignupResponse signup(UserSignupRequest signupRequest);

    LoginResponse login(LoginRequest loginRequest);
}
