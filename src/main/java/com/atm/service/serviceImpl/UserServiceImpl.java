package com.atm.service.serviceImpl;

import com.atm.common.Role;
import com.atm.dto.LoginRequest;
import com.atm.dto.LoginResponse;
import com.atm.dto.UserSignupRequest;
import com.atm.dto.UserSignupResponse;
import com.atm.exception.InvalidUsernamePasswordException;
import com.atm.exception.PasswordMissMatchException;
import com.atm.model.User;
import com.atm.repository.UserRepository;
import com.atm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserSignupResponse signup(UserSignupRequest signupRequest) {
        if(!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())){
            throw new PasswordMissMatchException("Password does not match");
        }

        User entity =new User();
        entity.setUsername(signupRequest.getUsername());
        entity.setNic(signupRequest.getNic());
        entity.setEmail(signupRequest.getEmail());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        Role role = Role.valueOf(signupRequest.getRole().toUpperCase());
        entity.setRole(role);
        userRepository.save(entity);
        log.info("User saved successfully, username:  {}", entity.getUsername());


        UserSignupResponse userSignupResponse = new UserSignupResponse();
        userSignupResponse.setUsername(entity.getUsername());
        userSignupResponse.setNic(entity.getNic());
        String userRole = entity.getRole().toString();
        userSignupResponse.setRole(userRole);
        userSignupResponse.setEmail(entity.getEmail());
        userSignupResponse.setPassword(entity.getPassword());
        userSignupResponse.setPassword(entity.getPassword());
        userSignupResponse.setConfirmPassword(entity.getPassword());
        userSignupResponse.setCreatedAt(entity.getCreatedAt());
        return userSignupResponse;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            log.info("User authenticated: {}!",loginRequest.getEmail());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setMessage("Login Successful!");
            return loginResponse;

        }catch (BadCredentialsException ae){
            log.error("User not authenticated: {}!",loginRequest.getEmail());
            throw new InvalidUsernamePasswordException("Bad Credentials!", loginRequest.getEmail());
        }
    }
}
