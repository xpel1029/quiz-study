package com.quiz_study.repository;

import com.quiz_study.domain.Quiz;
import com.quiz_study.domain.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByStudyNote(StudyNote studyNote);
}
