package com.chatbotgen.aplicacao.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de renovação de token.
 * 
 * @param refreshToken Token de renovação
 * @author Sistema
 */
public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token é obrigatório")
    String refreshToken
) {
}

