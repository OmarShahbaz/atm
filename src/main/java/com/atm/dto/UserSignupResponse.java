package com.atm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSignupResponse {

    private String username;

    private String nic;

    private String email;

    @JsonIgnore
    private String password; //Jackson don't serialize or deserialize

    @JsonIgnore
    private String confirmPassword; //field not even required, just for test case

    private LocalDateTime createdAt;

    private String role;

}
