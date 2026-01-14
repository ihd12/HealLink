package org.zerock.obj2026.healthinfo.config;/*
package org.zerock.obj2026.healthinfo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 일단 끄기
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ⭐ 모든 요청을 로그인 없이 허용!
                )
                .formLogin(login -> login.disable()) // 기본 로그인 폼 끄기
                .logout(logout -> logout.logoutSuccessUrl("/")); // 로그아웃 설정

        return http.build();
    }
}*/
