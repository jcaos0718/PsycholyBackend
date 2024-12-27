package com.psicolApp.apirest.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psicolApp.apirest.etities.Note;
import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.repositorys.NoteRepository;
import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.utils.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Note createNote(Note note) {
        note.setDate(LocalDateTime.now());
        return noteRepository.save(note);
    }

    
    public Optional<Note> getNoteByIdAndUsername(Long id, String username) {
        return noteRepository.findByIdAndPatientUsername(id, username);
    }

  

    public Note updateNoteByIdAndUsername(Long id, String username, Note noteDetails) {
        Note note = noteRepository.findByIdAndPatientUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id + " for user " + username));
        note.setContent(noteDetails.getContent());
        return noteRepository.save(note);
    }

    public void deleteNoteByIdAndUsername(Long id, String username) {
        Note note = noteRepository.findByIdAndPatientUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id + " for user " + username));
        noteRepository.delete(note);
    }

    public List<Note> getNotesByUsername(String username) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username " + username));
        return noteRepository.findByPatient(patient);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }


    


}