package com.atm.exception;

import java.math.BigDecimal;

public class AmountLessThenZeroException extends RuntimeException{

    private String key;

    private String Value;

    public AmountLessThenZeroException(String message, String key, BigDecimal value){
        super(message);
        this.key = key;
        this.Value = value.toString();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return Value;
    }
}
