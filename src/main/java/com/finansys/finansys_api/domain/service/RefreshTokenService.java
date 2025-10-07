package com.finansys.finansys_api.domain.service;
import com.finansys.finansys_api.domain.model.RefreshToken;
import com.finansys.finansys_api.domain.model.User;
import com.finansys.finansys_api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.jwt.refresh-expiration:2592000}") // 30 dias em segundos
    private long refreshTokenExpiration;
    public RefreshToken createRefreshToken(User user) {
        // Revoga todos os tokens existentes do usuário
        refreshTokenRepository.revokeAllUserTokens(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration));
        refreshToken.setIsRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenAndIsRevokedFalse(token);
    }
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Faça login novamente.");
        }
        return token;
    }
    public void revokeToken(RefreshToken token) {
        token.setIsRevoked(true);
        refreshTokenRepository.save(token);
    }
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredAndRevokedTokens();
    }
}