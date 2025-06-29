package com.atm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountUpdateRequest {


    @NotBlank(message = "email is required")
    private String accountEmail;

    @NotBlank(message = "name is required")
    private String accountHolderName;


}
