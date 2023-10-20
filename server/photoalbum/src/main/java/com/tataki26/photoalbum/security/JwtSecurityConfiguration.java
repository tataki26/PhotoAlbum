package com.tataki26.photoalbum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtSecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // apply authentication to all requests
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                // set session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // disable http basic authentication (use jwt instead)
                .httpBasic(HttpBasicConfigurer::disable)
                // csrf disable
                .csrf(CsrfConfigurer::disable)
                // same origin request -> enable frame option (h2-console)
                .headers(customizer -> customizer.frameOptions(frame -> frame.sameOrigin()))
                // enable jwt authentication within oauth2 resource server
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
