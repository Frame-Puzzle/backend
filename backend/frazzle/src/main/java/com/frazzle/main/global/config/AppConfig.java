package com.frazzle.main.global.config;

import com.frazzle.main.global.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
