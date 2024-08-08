package com.frazzle.main.global.config;

import com.frazzle.main.domain.auth.enums.UserRole;
import com.frazzle.main.global.filter.JwtFilter;
import com.frazzle.main.domain.auth.service.JwtTokenService;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //http 보안 설정으로 특정 url에 접근 권한 제한
    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        //모두 접근 허용
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**","/login/**", "/token/refresh").permitAll()
                        //유저만 접근 허용
                        .requestMatchers("/user/**").hasAuthority(UserRole.USER.getRole())
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) // 로그인 폼 미사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) // http basic 미사용
                .addFilterBefore(new JwtFilter(jwtTokenService, userService), UsernamePasswordAuthenticationFilter.class) // JWT Filter 추가
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // JwtFilter 에서 CustomException 사용하기 위해 추가
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 아래 url은 filter 에서 제외
        return web ->
                web.ignoring()
                        .requestMatchers("/login/**", "/token/refresh", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/ws/**");
    }
}