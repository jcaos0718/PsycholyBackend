package com.psicolApp.apirest.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psicolApp.apirest.dto.AuthResponse;
import com.psicolApp.apirest.dto.LoginRequest;
import com.psicolApp.apirest.dto.RegisterRequest;
import com.psicolApp.apirest.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping( value = "login")

    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    // @PreAuthorize("hasAuthority('PSYCHOLOGIST') or  hasAuthority('ADMIN')")
    @PostMapping(value = "registerPatiente")

    public ResponseEntity<AuthResponse> registerPatient(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.patientRegister(request));
    }


    @PostMapping(value = "registerPsychologist")
    public ResponseEntity<AuthResponse> registerPsychologist(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.psychologistRegister(request));
    }
    

}
