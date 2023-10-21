package com.tataki26.photoalbum.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtSecurityConfiguration {
    private final String[] allowedUrls = {"/users", "/sign-in"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // apply authentication to all requests
                .authorizeHttpRequests(request ->
                        request.requestMatchers(allowedUrls).permitAll()
                               .requestMatchers(PathRequest.toH2Console()).permitAll()
                               .anyRequest().authenticated()
                )
                // set session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // disable http basic authentication (use jwt instead)
                .httpBasic(HttpBasicConfigurer::disable)
                // csrf disable
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
