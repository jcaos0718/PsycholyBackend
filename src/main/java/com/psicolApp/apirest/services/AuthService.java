package com.psicolApp.apirest.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.psicolApp.apirest.utils.JwtService;

import com.psicolApp.apirest.dto.AuthResponse;
import com.psicolApp.apirest.dto.LoginRequest;
import com.psicolApp.apirest.dto.RegisterRequest;
import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.etities.Psychologist;
import com.psicolApp.apirest.etities.Role;
import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.repositorys.PsychologistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final  PatientRepository patientRepository;
    private final  PsychologistRepository psychologistRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
   
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        
       Optional<Patient> patientOpt = patientRepository.findByUsername(request.getUsername());

       if (patientOpt.isPresent()) { 

            Patient user = patientOpt.get();
            
            String token=jwtService.getToken(user);
            return AuthResponse.builder()
                .token(token)
                .build();
                


       }
    Optional<Psychologist> psychologistOpt = psychologistRepository.findByUsername(request.getUsername());
    if (psychologistOpt.isPresent()) {
        Psychologist psychologist = psychologistOpt.get();
        // Generar el token JWT para el psicólogo
        String token = jwtService.getToken(psychologist);
        // Construir y retornar la respuesta de autenticación con el token generado
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    throw new UsernameNotFoundException("User not found");



       

    }

    public AuthResponse patientRegister(RegisterRequest request) {
        
       Patient user = Patient.builder()
       .username(request.getUsername())
       .password(passwordEncoder.encode( request.getPassword()))
       .firstname(request.getFirstname())
       .lastname(request.getLastname())
       .country(request.getCountry())
       .role(Role.PATIENT)
       .build();
       patientRepository.save(user);

       return AuthResponse.builder()
       .token(jwtService.getToken(user))
       .build();

    }

    public AuthResponse psychologistRegister(RegisterRequest request) {

        Psychologist user =     Psychologist.builder()
       .username(request.getUsername())
       .password(passwordEncoder.encode( request.getPassword()))
       .role(Role.PSYCHOLOGIST)
       .firstname(request.getFirstname())
       .lastname(request.getLastname())
       .country(request.getCountry())
       .build();
       psychologistRepository.save(user);

       return AuthResponse.builder()
       .token(jwtService.getToken(user))
       .build();
   
    }

}
