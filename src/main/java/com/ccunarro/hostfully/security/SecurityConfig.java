package com.ccunarro.hostfully.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SimpleUserDetailsService simpleUserDetailsService;

    public SecurityConfig(SimpleUserDetailsService simpleUserDetailsService) {
        this.simpleUserDetailsService = simpleUserDetailsService;
    }


    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(simpleUserDetailsService)
                .headers(headers -> headers.frameOptions().sameOrigin())
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(1)
    SecurityFilterChain allowedEndpointsSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/h2-console/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/*")
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                            AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                            AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                            AntPathRequestMatcher.antMatcher("/api-docs/*")).permitAll();
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                        AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                        AntPathRequestMatcher.antMatcher("/api-docs/*")))
                .headers(headers -> headers.frameOptions().disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}