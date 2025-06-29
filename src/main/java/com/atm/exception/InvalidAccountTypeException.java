package com.atm.exception;

public class InvalidAccountTypeException extends RuntimeException {

    private final String key;
    private final String value;

    public InvalidAccountTypeException(String message, String key, String invalidValue){
        super(message);
        this.key = key;
        this.value = invalidValue;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


}
