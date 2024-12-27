package com.psicolApp.apirest.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.etities.Psychologist;
import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.repositorys.PsychologistRepository;
import com.psicolApp.apirest.utils.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatientPsychologist(String username, Long psychologistId) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username " + username));
        Psychologist psychologist = psychologistRepository.findById(psychologistId)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with id " + psychologistId));
        patient.setPsychologist(psychologist);
        return patientRepository.save(patient);
    }

    public List<Patient> getPatientsByPsychologistUsername(String psychologistUsername) {
        Psychologist psychologist = psychologistRepository.findByUsername(psychologistUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with username " + psychologistUsername));
        return patientRepository.findByPsychologist(psychologist);
    }

    public Optional<Patient> findByUsername(String username) {
        return patientRepository.findByUsername(username);
    }

    
}