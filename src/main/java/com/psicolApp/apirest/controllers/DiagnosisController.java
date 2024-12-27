package com.psicolApp.apirest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.psicolApp.apirest.dto.DiagnosisDTO;
import com.psicolApp.apirest.dto.PatientDiagnosesDTO;
import com.psicolApp.apirest.etities.Diagnosis;
import com.psicolApp.apirest.etities.Note;
import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.etities.Psychologist;
import com.psicolApp.apirest.repositorys.DiagnosisRepository;
import com.psicolApp.apirest.repositorys.PsychologistRepository;
import com.psicolApp.apirest.services.DiagnosisService;
import com.psicolApp.apirest.services.PatientService;
import com.psicolApp.apirest.utils.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {


    @Autowired
    private PsychologistRepository psychologistRepository;

    private final DiagnosisService diagnosisService;
    private final PatientService patientService;


    public DiagnosisController(DiagnosisService diagnosisService, PatientService patientService) {
        this.diagnosisService = diagnosisService;
        this.patientService = patientService;
        
    }

    //HIsotial clinico (Sin probar)
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Diagnosis>> getDiagnosesByPatient(@PathVariable Long patientId) {
        List<Diagnosis> diagnoses = diagnosisService.getDiagnosesByPatientId(patientId);
        return ResponseEntity.ok(diagnoses);
    }



    // Lista de diagnostico para psicologo
    @GetMapping("/by-psychologist")
    public ResponseEntity<List<Diagnosis>> getDiagnosesByPsychologist2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String psychologistUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Diagnosis> diagnoses = diagnosisService.getDiagnosesByPsychologistUsername2(psychologistUsername);
        return ResponseEntity.ok(diagnoses);
    }

    @GetMapping("/by-psychologist/patients")
public ResponseEntity<List<PatientDiagnosesDTO>> getDiagnosesByPsychologist() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String psychologistUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
    List<Diagnosis> diagnoses = diagnosisService.getDiagnosesByPsychologistUsername2(psychologistUsername);

    // Convertir cada Diagnosis a DiagnosisDTO
    List<DiagnosisDTO> diagnosisDTOs = diagnoses.stream().map(diagnosis -> {
        String patientUsername = diagnosis.getPatient().getUsername();
        String patientFirstname = diagnosis.getPatient().getFirstname();
        String patientLastname = diagnosis.getPatient().getLastname();

        String psychologistUsernameDTO = diagnosis.getPsychologist().getUsername();
        String psychologistFirstname = diagnosis.getPsychologist().getFirstname();
        String psychologistLastname = diagnosis.getPsychologist().getLastname();

        return new DiagnosisDTO(
            diagnosis.getId(),
            diagnosis.getDescription(),
            diagnosis.getContent(),
            diagnosis.getDiagnosisDate(),
            patientUsername,
            patientFirstname,
            patientLastname,
            psychologistUsernameDTO,
            psychologistFirstname,
            psychologistLastname
        );
    }).collect(Collectors.toList());

    // Agrupar los DiagnosisDTO por el username del paciente
    Map<String, List<DiagnosisDTO>> diagnosisDTOsByPatient = diagnosisDTOs.stream()
            .collect(Collectors.groupingBy(DiagnosisDTO::getPatientUsername));

    // Convertir a una lista de PatientDiagnosesDTO
    List<PatientDiagnosesDTO> patientDiagnosesList = diagnosisDTOsByPatient.entrySet().stream()
            .map(entry -> new PatientDiagnosesDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

    return ResponseEntity.ok(patientDiagnosesList);
}


    //Crear diagnostico para un paciente

    @PostMapping("/{patientUsername}")
    public ResponseEntity<DiagnosisDTO> addDiagnosis(@RequestBody Diagnosis diagnosis, @PathVariable String patientUsername) {
        Patient patient = patientService.findByUsername(patientUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username: " + patientUsername));
        diagnosis.setPatient(patient);
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String psychologistUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Psychologist> psychologistOptional = psychologistRepository.findByUsername(psychologistUsername);
        if (psychologistOptional.isPresent()) {
            Psychologist psychologist = psychologistOptional.get();
            diagnosis.setPsychologist(psychologist);
        }
    
        Diagnosis savedDiagnosis = diagnosisService.save(diagnosis);
        DiagnosisDTO diagnosisDTO = convertToDTO(savedDiagnosis);
    
        return ResponseEntity.ok(diagnosisDTO);
    }


    @PutMapping("/{patientUsername}/{id}")
    public ResponseEntity<DiagnosisDTO> updateNote(@PathVariable Long id, @PathVariable String patientUsername, @RequestBody Diagnosis diagnosisDetails) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DiagnosisDTO diagnosisDTO = convertToDTO(diagnosisService.updateDiagnosisByIdAndUsername(id, patientUsername,  diagnosisDetails));
        
        // String usernamePshychologist = ((UserDetails) authentication.getPrincipal()).getUsername();
        diagnosisDTO.setContent((diagnosisDetails.getContent()));
        return ResponseEntity.ok(diagnosisDTO );
    }


    @DeleteMapping("/{patientUsername}/{id}")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long id,@PathVariable String patientUsername) {

        diagnosisService.deleteNoteByIdAndUsername(id, patientUsername);
        return ResponseEntity.noContent().build();
    }


    private DiagnosisDTO convertToDTO(Diagnosis diagnosis) {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setId(diagnosis.getId());
        diagnosisDTO.setDescription(diagnosis.getDescription());
        diagnosisDTO.setDiagnosisDate(diagnosis.getDiagnosisDate());
        diagnosisDTO.setPatientUsername(diagnosis.getPatient().getUsername());
        diagnosisDTO.setPatientFirstname(diagnosis.getPatient().getFirstname());
        diagnosisDTO.setPatientLastname(diagnosis.getPatient().getLastname());
        diagnosisDTO.setPsychologistUsername(diagnosis.getPsychologist().getUsername());
        diagnosisDTO.setPsychologistFirstname(diagnosis.getPsychologist().getFirstname());
        diagnosisDTO.setPsychologistLastname(diagnosis.getPsychologist().getLastname());
        return diagnosisDTO;
    }
}