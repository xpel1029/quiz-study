package com.quiz_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuizStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizStudyApplication.class, args);
	}

}
