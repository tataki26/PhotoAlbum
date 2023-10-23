package com.tataki26.photoalbum.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwtSecurityConfiguration {
    private final String[] allowedUrls = {"/users", "/sign-in"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // apply authentication to all requests
                .authorizeHttpRequests(request ->
                        request.requestMatchers(allowedUrls).permitAll()
                               // .requestMatchers(PathRequest.toH2Console()).permitAll()
                               .anyRequest().authenticated()
                )
                // set session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // disable http basic authentication (use jwt instead)
                .httpBasic(HttpBasicConfigurer::disable)
                // csrf disable
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // create key pair (2048 bits) for RSAKey
    // key pair: public key & private key
    @Bean
    public KeyPair keyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // create RSA key
    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey
                // set public key - verify jwt
                .Builder((RSAPublicKey) keyPair.getPublic())
                // set private key - create jwt (header, payload)
                .privateKey(keyPair.getPrivate())
                // set key id (unique id for RSAKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    // JWK(JSON Web Key)
    @Bean
    // JWKSource provides the information of jwt public key
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        // JWKSet: the set of JWK
        JWKSet jwkSet = new JWKSet(rsaKey);
        // Return a JWKSource implementation using a lambda expression
        return ((jwkSelector, context) -> jwkSelector.select(jwkSet));
    }

    // create JwtDecoder
    // use public key to verify signature
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /*
    @Bean
    public H2ConsoleProperties h2ConsoleProperties() {
        return new H2ConsoleProperties();
    }
    */
}
