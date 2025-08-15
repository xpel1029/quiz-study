package com.quiz_study.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quizzes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_note_id", nullable = false)
    private StudyNote studyNote;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    private String explanation; // 해설

    private boolean correct; // 사용자가 마지막으로 맞췄는지 여부
}