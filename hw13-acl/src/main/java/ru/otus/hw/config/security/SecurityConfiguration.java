package ru.otus.hw.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/books", "/books/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/books/delete/{id}").access(
                                new WebExpressionAuthorizationManager(
                                        "hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE') || " +
                                                "hasAuthority('ROLE_ADMIN')")
                        )
                        .requestMatchers(HttpMethod.POST, "/books/edit").access(
                                new WebExpressionAuthorizationManager(
                                        "hasPermission(#id, 'ru.otus.hw.models.Book', 'WRITE') || " +
                                                "hasAuthority('ROLE_ADMIN')")
                        )
                        .anyRequest().authenticated())
                .formLogin(fm -> fm
                        .loginPage("/login")
                )
                .exceptionHandling(handler -> handler.accessDeniedPage("/forbidden"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
