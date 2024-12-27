package com.psicolApp.apirest.etities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String description;
    private String content;
    private LocalDateTime diagnosisDate;
   

    @ManyToOne
    @JoinColumn(name = "psychologist_id")
    private Psychologist psychologist;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    //Contructor
    

  
    

}
