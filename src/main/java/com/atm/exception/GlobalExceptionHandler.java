package com.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(AccountNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put(ex.getKey(), ex.getValue());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMissMatch(PasswordMissMatchException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AmountLessThenZeroException.class)
    public ResponseEntity<Map<String, String>> handleAmount(AmountLessThenZeroException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message",ex.getMessage());
        error.put(ex.getKey(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidUsernamePasswordException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(InvalidUsernamePasswordException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message",ex.getMessage());
        error.put("email", ex.getEmail());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateAccountTypeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAccountType(DuplicateAccountTypeException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message",ex.getMessage());
        error.put(ex.getKey(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put(ex.getKey(), ex.getValue());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAccountType(InvalidAccountTypeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put(ex.getKey(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
