package com.quiz_study.service;

import com.quiz_study.domain.StudyNote;
import com.quiz_study.repository.StudyNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final StudyNoteRepository noteRepository;

    @Override
    public StudyNote createNote(StudyNote note) {
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Override
    public Optional<StudyNote> getNote(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    public List<StudyNote> getNotes() {
        return noteRepository.findAll();
    }

    @Override
    public StudyNote updateNote(Long id, StudyNote note) {
        return noteRepository.findById(id).map(existing -> {
            existing.setTitle(note.getTitle());
            existing.setContent(note.getContent());
            existing.setUpdatedAt(LocalDateTime.now());
            return noteRepository.save(existing);
        }).orElseThrow();
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
