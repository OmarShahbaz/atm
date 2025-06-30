package com.atm.config;

import com.atm.exception.security.CustomAccessDeniedHandler;
import com.atm.exception.security.CustomAuthenticationEntryPoint;
import com.atm.jwt.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtFilter jwtFilter;

    public SecurityConfig(AuthService authService, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint, JwtFilter jwtFilter){
        this.authService = authService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttp ->
                    authorizeHttp
                            .requestMatchers("/v1/user/**").permitAll()
                            .requestMatchers("/v2/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint));
                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("create object of BCryptPasswordEncoder of type PasswordEncoder in IoC Container..");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder());
        log.info("create instance of DaoAuthenticationProvider in IoC Container..");
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("create a bean of type AuthenticationManager in IoC Container..");
        return config.getAuthenticationManager();
    }


}
