package com.atm.exception;

public class UserNotFoundException extends RuntimeException{
    private String key;

    private String value;

    public UserNotFoundException(String message, String key, String value){
        super(message);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
