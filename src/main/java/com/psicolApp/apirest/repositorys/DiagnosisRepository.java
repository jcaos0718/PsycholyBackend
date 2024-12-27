package com.psicolApp.apirest.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psicolApp.apirest.etities.Diagnosis;
import com.psicolApp.apirest.etities.Note;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByPatientId(Long patientId);
    List<Diagnosis> findByPatientPsychologistUsername(String psychologistUsername);
    List<Diagnosis> findByPsychologistUsername(String psychologistUsername);
    Optional<Diagnosis> findByIdAndPatientUsername(Long id, String username);
}
