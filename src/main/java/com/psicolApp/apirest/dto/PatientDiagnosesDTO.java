package com.psicolApp.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDiagnosesDTO {
    private String patientUsername;
    private List<DiagnosisDTO> diagnoses;
}