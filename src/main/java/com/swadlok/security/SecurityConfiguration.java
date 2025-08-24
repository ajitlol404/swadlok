package com.swadlok.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.swadlok.utility.AppConstant.*;
import static java.lang.Boolean.TRUE;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    // Static resources excluded from authentication
    private static final String[] STATIC_RESOURCES = {
            "/css/**", "/images/**",
            "/javascript/**", "/favicon.ico",
            "/webjars/**",
    };

    private static final String[] PUBLIC_REST_APIS = {
            BASE_API_PATH + "/unverified-users/**",
            BASE_API_PATH + "/enumeration-values/**",
            BASE_API_PATH + "/setup/**"
    };

    private static final String[] PUBLIC_APIS = {
            "/setup/**",
            "/signin",
            "/unverified-users/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            ADMIN_BASE_API_PATH + "/**",
            ADMIN_BASE_PATH + "/**"
    };

    private static final String[] CUSTOMER_ENDPOINTS = {
            CUSTOMER_BASE_API_PATH + "/**",
            CUSTOMER_BASE_PATH + "/**"
    };

    private static final String[] DELIVERY_ENDPOINTS = {
            DELIVERY_BASE_API_PATH + "/**",
            DELIVERY_BASE_PATH + "/**"
    };

    private static final String[] MANAGER_ENDPOINTS = {
            MANAGER_BASE_API_PATH + "/**",
            MANAGER_BASE_PATH + "/**"
    };


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(
                                        Stream.of(STATIC_RESOURCES, PUBLIC_REST_APIS, PUBLIC_APIS)
                                                .flatMap(Arrays::stream)
                                                .toArray(String[]::new)
                                ).permitAll()
                                .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
                                .requestMatchers(CUSTOMER_ENDPOINTS).hasRole("CUSTOMER")
                                .requestMatchers(MANAGER_ENDPOINTS).hasRole("MANAGER")
                                .requestMatchers(DELIVERY_ENDPOINTS).hasRole("DELEVIERY")
                                .anyRequest()
                                .authenticated())
                .formLogin(login -> login
                                .loginPage("/signin")
                                .loginProcessingUrl("/signin-process")
                                .failureUrl("/signin-fail")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                        /*.defaultSuccessUrl("/", true)*/)
                .logout(logout -> logout
                        .logoutUrl("/signout")
                        .logoutSuccessUrl("/signout-success")
                        .invalidateHttpSession(TRUE)
                        .clearAuthentication(TRUE)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .headers(h ->
                        h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                .httpStrictTransportSecurity(
                                        hstsConfig -> hstsConfig
                                                .includeSubDomains(TRUE)
                                                .maxAgeInSeconds(Duration.ofDays(365)
                                                        .toSeconds())
                                )
                                .xssProtection(xssProtectionConfig -> xssProtectionConfig
                                        .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                .contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy
                                        .policyDirectives(
                                                "default-src 'self'; " +
                                                        "script-src 'self'; " +
                                                        "img-src 'self' data: blob:; " +
                                                        "font-src 'self'; " +
                                                        "style-src 'self' 'sha256-hMzqs20LuQL1AJI7RBQ1EGzMJRKMCUEeEbrhKZ8Z2Vg=' 'sha256-g0/XswAmA05eJqlbjX+QanYdtwQs9NJHFaBXC9/dmm8=';"
                                        ))
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(IF_REQUIRED)
                                .maximumSessions(2)
                                .maxSessionsPreventsLogin(TRUE)
                );
        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
