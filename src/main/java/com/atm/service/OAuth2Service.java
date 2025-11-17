package com.atm.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OAuth2Service {

    ResponseEntity<Map<String, String>> handleCallback(String code);
}
