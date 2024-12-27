package com.psicolApp.apirest.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.psicolApp.apirest.dto.NoteDTO;
import com.psicolApp.apirest.etities.Note;
import com.psicolApp.apirest.etities.Patient;
import com.psicolApp.apirest.repositorys.PatientRepository;
import com.psicolApp.apirest.services.CalificationService;
import com.psicolApp.apirest.services.NoteService;
import com.psicolApp.apirest.services.PatientService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CalificationService calificationService; // Servicio para ejecutar el script de Python


    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return noteService.getNoteByIdAndUsername(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        
        return ResponseEntity.ok(noteService.updateNoteByIdAndUsername(id, username, noteDetails));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        noteService.deleteNoteByIdAndUsername(id, username);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/notes")
    public ResponseEntity<List<NoteDTO>> getUserNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<Note> notes = noteService.getNotesByUsername(username);
        
        List<NoteDTO> noteDTOs = notes.stream().map(note -> {
            String patientUsername = note.getPatient().getUsername();
            String patientFirstname = note.getPatient().getFirstname();
            String patientLastname = note.getPatient().getLastname();
            
            return new NoteDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCalification(),
                patientUsername,
                patientFirstname,
                patientLastname,
                note.getDate() // Pasar el campo date aquí
            );
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(noteDTOs);
    }
    @PostMapping
    public ResponseEntity<String> addNote(@RequestBody Note note) {
        // Guarda la nota en la base de datos
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
    
        Optional<Patient> patientOptional = patientService.findByUsername(username);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            note.setPatient(patient); // Setear el paciente en la nota
        } else {
            return ResponseEntity.status(404).body("Patient not found");
        }
        Note savedNote = noteService.createNote(note);

        

        // Califica la nota utilizando el servicio de Python
        try {
            String evaluationResult = calificationService.evaluateText(savedNote.getContent());
            savedNote.setCalification(evaluationResult); // Setear la calificación en la nota
            noteService.updateNote(savedNote); // Actualizar la nota con la calificación
            return ResponseEntity.ok("Note added and evaluated. Evaluation result: " + evaluationResult);

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error adding or evaluating the note.");
        }
    }

   
}