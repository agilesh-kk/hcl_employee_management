
package service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entity.RefreshToken;
import entity.User;

import repository.RefreshTokenRepository;
import repository.UserRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private long refreshTokenDurationMs;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    // Generate and store a refresh token
    @Transactional
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDate = LocalDateTime.now()
                .plus(Duration.ofMillis(refreshTokenDurationMs));

        RefreshToken refreshToken = new RefreshToken(
                token,
                expiryDate,
                user
        );

        return refreshTokenRepository.save(refreshToken);
    }

    // Validate refresh token
    public RefreshToken verifyExpiration(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refresh token not found"));

        if (!refreshToken.getExpiryDate().isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Refresh token has expired. Please login again.");
        }

        return refreshToken;
    }

    // Rotate refresh token
    @Transactional
    public RefreshToken renewRefreshToken(String token) {

        RefreshToken oldToken = verifyExpiration(token);

        User user = oldToken.getUser();

        refreshTokenRepository.delete(oldToken);

        return createRefreshToken(user.getUsername());
    }

    // Revoke all refresh tokens for a user
    @Transactional
    public void revokeAllUserTokens(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        refreshTokenRepository.deleteByUserId(user.getId());
    }
    
 // Revoke a refresh token during logout
    @Transactional
    public void revokeToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException("Refresh token not found"));

        refreshTokenRepository.delete(refreshToken);
    }
}
