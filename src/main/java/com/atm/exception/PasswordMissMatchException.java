package com.atm.exception;

public class PasswordMissMatchException extends RuntimeException{

    public PasswordMissMatchException(String message){
        super(message);

    }
}
