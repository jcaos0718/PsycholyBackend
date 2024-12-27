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
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private String calification;
    private String patientUsername;
    private String patientFirstname;
    private String patientLastname;
    private LocalDateTime Date;
}