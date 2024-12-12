package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS
                .and()
                .csrf().disable() // Disable CSRF for simplicity; adjust as needed
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Permit all requests; adjust based on your security requirements
                )
                .httpBasic(Customizer.withDefaults()); // Use basic HTTP authentication; adjust as needed

        return http.build();
    }
}