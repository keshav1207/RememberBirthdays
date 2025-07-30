package com.example.RememberBirthdays.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/public/**"). permitAll()
                            .anyRequest().authenticated()

                    )
                    .oauth2ResourceServer(oauth2 ->oauth2.jwt(Customizer.withDefaults()));
            return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Replace with your real issuer URI or JWKS endpoint
        String jwkSetUri = "http://localhost:8080/realms/rememberBirthdays-realm/protocol/openid-connect/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
