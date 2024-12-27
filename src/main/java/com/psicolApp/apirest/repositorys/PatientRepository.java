package com.psicolApp.apirest.repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.etities.Psychologist;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);
    List<Patient> findByPsychologist(Psychologist psychologist);
    
}