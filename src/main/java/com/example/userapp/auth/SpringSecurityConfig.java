package com.example.userapp.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            return http.authorizeHttpRequests(authz ->
                 authz.requestMatchers(HttpMethod.GET, "/api/users", "api/users/page/{page}").permitAll()
                        .anyRequest().authenticated()
            ).csrf(config -> {
                        config.disable();
                    })
                    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
