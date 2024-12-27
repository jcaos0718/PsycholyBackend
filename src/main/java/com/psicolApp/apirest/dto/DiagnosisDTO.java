package com.psicolApp.apirest.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Getters y setters
@Builder //contruir Objs
@AllArgsConstructor 
@NoArgsConstructor

public class DiagnosisDTO {

    private Long id;
    private String description;
    private String content;
    private LocalDateTime diagnosisDate;
    private String patientUsername;
    private String patientFirstname;
    private String patientLastname;
    private String psychologistUsername;
    private String psychologistFirstname;
    private String psychologistLastname;


}
