package com.atm.exception;



public class InvalidUsernamePasswordException extends RuntimeException {

    private String email;

    public InvalidUsernamePasswordException(String message, String email){
        super(message + "Please check email/password.");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
