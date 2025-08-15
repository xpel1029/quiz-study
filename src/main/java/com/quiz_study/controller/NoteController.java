package com.quiz_study.controller;

import com.quiz_study.domain.StudyNote;
import com.quiz_study.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public String listNotes(Model model) {
        model.addAttribute("notes", noteService.getNotes());
        return "notes/list"; // Thymeleaf 템플릿
    }

    @GetMapping("/{id}")
    public String detailNote(@PathVariable Long id, Model model) {
        StudyNote note = noteService.getNote(id).orElseThrow();
        model.addAttribute("note", note);
        return "notes/detail";
    }

    @PostMapping
    public String createNote(@ModelAttribute StudyNote note) {
        noteService.createNote(note);
        return "redirect:/notes";
    }
}
