package com.psicolApp.apirest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psicolApp.apirest.etities.Diagnosis;
import com.psicolApp.apirest.etities.Note;
import com.psicolApp.apirest.repositorys.DiagnosisRepository;
import com.psicolApp.apirest.utils.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    public List<Diagnosis> getDiagnosesByPatientId(Long patientId) {
        return diagnosisRepository.findByPatientId(patientId);
    }

    public List<Diagnosis> getDiagnosesByPsychologistUsername(String psychologistUsername) {
        return diagnosisRepository.findByPatientPsychologistUsername(psychologistUsername);
    }


    public Diagnosis save(Diagnosis diagnosis) {
        // Asigna la fecha actual si es necesario
        diagnosis.setDiagnosisDate(LocalDateTime.now());
        return diagnosisRepository.save(diagnosis);
    }

    public List<Diagnosis> getDiagnosesByPsychologistUsername2(String psychologistUsername) {
        return diagnosisRepository.findByPsychologistUsername(psychologistUsername);
    }

    public Diagnosis updateDiagnosisByIdAndUsername(Long id, String username, Diagnosis diagnosisDetails) {
        Diagnosis diagnosis = diagnosisRepository.findByIdAndPatientUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id + " for user " + username));
                diagnosis.setContent(diagnosisDetails.getContent());
                diagnosis.setDescription(diagnosisDetails.getDescription());
        return diagnosisRepository.save(diagnosis);
    }

    public void deleteNoteByIdAndUsername(Long id, String patientUsername) {
        Diagnosis diagnosis = diagnosisRepository.findByIdAndPatientUsername(id, patientUsername)
        .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id + " for user " + patientUsername));
        diagnosisRepository.delete(diagnosis);
    }
}
