package com.psicolApp.apirest.repositorys;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psicolApp.apirest.etities.Psychologist;


public interface PsychologistRepository extends JpaRepository<Psychologist, Long> {
    Optional<Psychologist> findByUsername(String username);
}