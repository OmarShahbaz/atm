package com.atm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckBalanceRequest {

    @NotBlank(message = "Account holder name is required")
    private String holderName;

    @NotBlank(message = "Account type name is required")
    private String accountType;
}
