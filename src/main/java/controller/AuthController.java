
package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import dto.RegisterRequest;
import dto.LoginRequest;
import dto.AuthResponse;
import dto.RefreshTokenRequest;
import dto.TokenResponse;

import entity.RefreshToken;

import service.AuthService;
import service.JwtService;
import service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // USER REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // USER LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    // REFRESH ACCESS TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        RefreshToken newRefreshToken =
                refreshTokenService.renewRefreshToken(request.getRefreshToken());

        String newAccessToken =
                jwtService.generateToken(
                        newRefreshToken.getUser().getUsername()
                );

        TokenResponse response = new TokenResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                "Token refreshed successfully"
        );

        return ResponseEntity.ok(response);
    }
    
 // USER LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenRequest request) {

        refreshTokenService.revokeToken(request.getRefreshToken());

        return ResponseEntity.ok("Logout successful");
    }
}
