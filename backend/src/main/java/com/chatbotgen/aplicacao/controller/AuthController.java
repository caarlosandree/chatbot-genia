package com.chatbotgen.aplicacao.controller;

import com.chatbotgen.aplicacao.dto.auth.JwtResponse;
import com.chatbotgen.aplicacao.dto.auth.LoginRequest;
import com.chatbotgen.aplicacao.dto.auth.RefreshTokenRequest;
import com.chatbotgen.aplicacao.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para operações de autenticação.
 * 
 * @author Sistema
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de tokens")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Realiza login do usuário.
     * 
     * @param request Dados de login (email e senha)
     * @return Tokens JWT (access e refresh)
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica um usuário e retorna tokens JWT")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Renova os tokens usando um refresh token.
     * 
     * @param request Refresh token
     * @return Novos tokens JWT
     */
    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Renova os tokens de acesso usando um refresh token válido")
    public ResponseEntity<JwtResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        JwtResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Realiza logout, invalidando o refresh token.
     * 
     * @param request Refresh token a ser invalidado
     * @return 204 No Content
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida o refresh token do usuário")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}

