package com.psicolApp.apirest.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.psicolApp.apirest.utils.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final  JwtAuthenticationFilter jwtAuthenticationFilter;
    private final  AuthenticationProvider  authProvider;
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception{
        return http
            .csrf(csrf ->
                csrf.disable())
            .authorizeHttpRequests(authResuest ->
            authResuest
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/api/notes/**").permitAll()
                .requestMatchers("/api/patients/**").permitAll()
                .requestMatchers("/api/diagnoses/**").permitAll()
                .requestMatchers("/auth/registerPatiente").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/registerPsychologist").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                )
            .sessionManagement(sessionManager -> 
                sessionManager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            .build();

    }


      @Bean
        public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
