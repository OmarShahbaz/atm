package com.atm.config;

import com.atm.model.User;
import com.atm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting login with email: {}", email);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found associated with : " + email));
        if(user != null) {
            log.info("Roles: {}", user.getRole());
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(new SimpleGrantedAuthority(user.getRole().name()))
                    .build();
        }
        throw new UsernameNotFoundException("User not found associated with : " + email);
    }
}
