package com.quiz_study.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RefreshRequest {
    private String refreshToken;
}
