package com.frazzle.main.global.config;

import com.frazzle.main.global.security.auth.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //보안 설정 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> csrfConfigurer.disable()) //csrf 비활성화 -> rest api이므로
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/","/api/v1", "/login**", "/error**", "/users/**").permitAll()
                                .anyRequest().authenticated()
                ); //모든 사용자는 /, /login, /error 페이지 접근 허용
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .loginPage("/login") //로그인 페이지
//                                .defaultSuccessUrl("/home") //로그인 후 페이지
//                                .failureUrl("/login?error") //실패 페이지
//                                .userInfoEndpoint(userInfoEndpoint ->
//                                        userInfoEndpoint
//                                                .userService(customOAuth2UserService())
//                                ) //사용자 정보 처리 메서드
//                );

        return http.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}