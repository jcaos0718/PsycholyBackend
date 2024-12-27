package com.psicolApp.apirest.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.repositorys.PsychologistRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationsConfig {


    private final  PatientRepository patientRepository;
    private final  PsychologistRepository psychologistRepository;

    @Bean
    public  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        
        return username -> {
            Optional<UserDetails> patient = patientRepository.findByUsername(username).map(UserDetails.class::cast);
            if (patient.isPresent()) {
                return patient.get();
            }
            Optional<UserDetails> psychologist = psychologistRepository.findByUsername(username).map(UserDetails.class::cast);
            if (psychologist.isPresent()) {
                return psychologist.get();
            }
            throw new UsernameNotFoundException("User not found");
        };
    }

}
