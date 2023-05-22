package com.example.e_ticket_uz_railway.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final String[] WHITE_PAGES = {
            "/",
            "/auth/**"
    };
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity.
                csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_PAGES).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .permitAll()
                .and()
                .build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
