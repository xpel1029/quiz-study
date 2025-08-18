package com.quiz_study.controller;

import com.quiz_study.dto.auth.LoginRequest;
import com.quiz_study.dto.auth.TokenResponse;
import com.quiz_study.dto.auth.RefreshRequest;
import com.quiz_study.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest req) {
        return ResponseEntity.ok(authService.refresh(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest req) {
        authService.logout(req.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
