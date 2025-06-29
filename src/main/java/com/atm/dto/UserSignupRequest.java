package com.atm.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSignupRequest {

    private String username;

    private String nic;

    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be 3 characters long")
    private String password;

    private String confirmPassword;

    private LocalDateTime createdAt;

    private String role;

}
