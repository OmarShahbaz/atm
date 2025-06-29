package com.atm.controller;

import com.atm.dto.LoginRequest;
import com.atm.dto.LoginResponse;
import com.atm.dto.UserSignupRequest;
import com.atm.dto.UserSignupResponse;
import com.atm.model.User;
import com.atm.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;


    private UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signUp(@Valid @RequestBody UserSignupRequest signupRequest){
        return new ResponseEntity<>(userService.signup(signupRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }
}
