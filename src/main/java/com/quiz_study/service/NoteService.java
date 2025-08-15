package com.quiz_study.service;

import com.quiz_study.domain.StudyNote;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    StudyNote createNote(StudyNote note);
    Optional<StudyNote> getNote(Long id);
    List<StudyNote> getNotes();
    StudyNote updateNote(Long id, StudyNote note);
    void deleteNote(Long id);
}
