package com.atm.controller;

import com.atm.service.OAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/auth/google")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;


    public OAuth2Controller(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> handleGoogleCallback(@RequestParam String code){
        return oAuth2Service.handleCallback(code);
    }
}