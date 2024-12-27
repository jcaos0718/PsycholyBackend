package com.psicolApp.apirest;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.etities.Psychologist;
import com.psicolApp.apirest.etities.Role;
import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.repositorys.PsychologistRepository;

@SpringBootApplication
public class ApirestApplication {


    @Autowired
    private PsychologistRepository psychologistRepository;

    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
            .directory("./") // Directorio donde se encuentra el archivo .env
            .filename(".env") // Nombre del archivo .env
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(ApirestApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            // Crear el psicólogo con el rol de administrador
            Psychologist psychologistA = new Psychologist();
            psychologistA.setUsername("admin");
            psychologistA.setPassword(passwordEncoder.encode("12345"));
            psychologistA.setFirstname("ADMIN");
            psychologistA.setLastname("User");
            psychologistA.setCountry("Country");
            psychologistA.setRole(Role.ADMIN);

            psychologistRepository.save(psychologistA);


              // Crear el psicólogo con el rol de psicólogo
            Psychologist psychologist = new Psychologist();
            psychologist.setUsername("PSYCHOLOGIST");
            psychologist.setPassword(passwordEncoder.encode("12345"));
            psychologist.setFirstname("John");
            psychologist.setLastname("Doe");
            psychologist.setCountry("Country");
            psychologist.setRole(Role.PSYCHOLOGIST);

            psychologistRepository.save(psychologist);

            // Crear el paciente y asociarlo con el psicólogo con rol de psicólogo
            Patient patient = new Patient();
            patient.setUsername("PATIENT");
            patient.setPassword(passwordEncoder.encode("12345"));
            patient.setFirstname("John");
            patient.setLastname("Doe");
            patient.setCountry("Country");
            patient.setRole(Role.PATIENT);
            patient.setPsychologist(psychologist);

            patientRepository.save(patient);

            Patient patient2 = new Patient();
            patient2.setUsername("PATIENT2");
            patient2.setPassword(passwordEncoder.encode("12345"));
            patient2.setFirstname("John");
            patient2.setLastname("Doe");
            patient2.setCountry("Country");
            patient2.setRole(Role.PATIENT);
            patient2.setPsychologist(psychologist);

            patientRepository.save(patient2);
        };
    }

    
}