package com.example.samplelogin.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login").permitAll() // 로그인 페이지는 모두 접근 가능
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                )
                .formLogin(formLogin -> formLogin
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );


        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * 커스텀 인증 성공 핸들러 함수
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }

    /**
     * 커스텀 인증 로직 핸들러 함수
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    /**
     * 커스텀 인증 실패 핸들러 함수
     */
    @Bean
    protected AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomFailureHandler();
    }

    /**
     * 커스텀 인증 전 필터 핸들러 함수
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        return new CustomAuthenticationFilter();
    }

    /**
     * 권한 투표 핸들러 함수 (구현 예정)
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = List.of(
                new CustomAccessDecisionVoter());
        return new UnanimousBased(decisionVoters);
    }
}
