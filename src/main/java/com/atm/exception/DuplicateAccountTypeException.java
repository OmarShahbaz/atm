package com.atm.exception;

public class DuplicateAccountTypeException extends RuntimeException{

    private final String key;

    private final String value;

    public DuplicateAccountTypeException(String message, String key, String value){
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
