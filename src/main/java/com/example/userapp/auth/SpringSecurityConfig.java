package com.example.userapp.auth;

import com.example.userapp.auth.filter.JwtAuthenticationFilter;
import com.example.userapp.auth.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            return http.authorizeHttpRequests(authz ->
                 authz.requestMatchers(HttpMethod.GET, "/api/users", "api/users/page/{page}", "api/users/{id}").permitAll()
                         .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
                         .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                    .addFilter(new JwtValidationFilter(authenticationManager()))
                         .csrf(config -> {
                        config.disable();
                    })
                    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
