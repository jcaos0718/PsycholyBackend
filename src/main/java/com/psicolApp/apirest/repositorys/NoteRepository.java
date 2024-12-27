package com.psicolApp.apirest.repositorys;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.psicolApp.apirest.etities.Note;
import com.psicolApp.apirest.etities.Patient;

public interface NoteRepository extends JpaRepository<Note, Long> {
      List<Note> findByPatient(Patient patient);
      Optional<Note> findByIdAndPatientUsername(Long id, String username);
}