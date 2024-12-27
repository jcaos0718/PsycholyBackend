package com.psicolApp.apirest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.services.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    //Asignar psicologo

    @PutMapping("/{patientUsername}/{psychologistId}")
    public ResponseEntity<Patient> updatePatientPsychologist(
            @PathVariable Long psychologistId,  @PathVariable String patientUsername) {
        Patient updatedPatient = patientService.updatePatientPsychologist(patientUsername, psychologistId);
        return ResponseEntity.ok(updatedPatient);
    }

    //Patients per Pschologist
    @GetMapping("/by-psychologist")
    public ResponseEntity<List<Patient>> getPatientsByPsychologist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String psychologistUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Patient> patients = patientService.getPatientsByPsychologistUsername(psychologistUsername);
        return ResponseEntity.ok(patients);
    }

    
}