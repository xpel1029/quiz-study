package com.quiz_study.service;

import com.quiz_study.domain.RefreshToken;
import com.quiz_study.domain.User;
import com.quiz_study.dto.auth.LoginRequest;
import com.quiz_study.dto.auth.TokenResponse;
import com.quiz_study.dto.auth.RefreshRequest;
import com.quiz_study.repository.RefreshTokenRepository;
import com.quiz_study.repository.UserRepository;
import com.quiz_study.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwt;

    @Transactional
    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        var claims = new HashMap<String, Object>();
        claims.put("name", user.getName());

        String access = jwt.generateAccessToken(user.getEmail(), claims);
        String refresh = jwt.generateRefreshToken(user.getEmail());

        // 기존 refresh 정리(옵션): 사용자 단일 세션만 유지하고 싶다면 이전 토큰 revoke
        // refreshTokenRepository.findAllByUserIdAndRevokedFalse(user.getId()) ... (확장)

        refreshTokenRepository.save(RefreshToken.builder()
                .token(refresh)
                .user(user)
                .expiresAt(Instant.now().plusMillis(getRefreshValidMsFromConfig()))
                .revoked(false)
                .build());

        return new TokenResponse(access, refresh, "Bearer");
    }

    @Transactional
    public TokenResponse refresh(RefreshRequest req) {
        RefreshToken rt = refreshTokenRepository.findByToken(req.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다."));

        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었거나 철회되었습니다.");
        }

        User user = rt.getUser();
        var claims = new HashMap<String, Object>();
        claims.put("name", user.getName());

        String newAccess = jwt.generateAccessToken(user.getEmail(), claims);

        // 🔄 Refresh Token Rotation (권장): 기존 토큰 revoke + 새 토큰 재발급
        rt.setRevoked(true);
        String newRefresh = jwt.generateRefreshToken(user.getEmail());
        refreshTokenRepository.save(RefreshToken.builder()
                .token(newRefresh)
                .user(user)
                .expiresAt(Instant.now().plusMillis(getRefreshValidMsFromConfig()))
                .revoked(false)
                .build());

        return new TokenResponse(newAccess, newRefresh, "Bearer");
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    private long getRefreshValidMsFromConfig() {
        // JwtTokenProvider 내부 값을 꺼내는 접근자가 없다면 properties에서 다시 읽거나 상수로 관리
        // 간단히 14일 고정으로 둠(위 properties와 일치)
        return 1209600000L;
    }
}
