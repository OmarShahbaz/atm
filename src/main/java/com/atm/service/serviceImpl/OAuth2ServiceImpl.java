package com.atm.service.serviceImpl;

import com.atm.common.Role;
import com.atm.jwt.JwtService;
import com.atm.model.User;
import com.atm.repository.UserRepository;
import com.atm.service.OAuth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private final RestTemplate restTemplate;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Value("${spring.secret.oauth2.client.registration.google.client-id}")
    private String clientId; //get value from application.properties

    @Value("${spring.secret.oauth2.client.registration.google.client-secret}")
    private String clientSecret; //get value from application.properties

    public OAuth2ServiceImpl(RestTemplate restTemplate, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Map<String, String>> handleCallback(String code) {
        try{
            //exchange authorization code for token
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "https://developers.google.com/oauthplayground");
            params.add("grant_type", "authorization_code");

            //creating http headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //key1=value1&key2=value2&..

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);


            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);

            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            if(userInfoResponse.getStatusCode() == HttpStatus.OK){
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try{
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (Exception e){
                    User user = new User();
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRole(Role.ROLE_USER);
                    userRepository.save(user);
                }
                String jwtToken = jwtService.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            log.error("Exception Occurred while handleGoogleCallback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
