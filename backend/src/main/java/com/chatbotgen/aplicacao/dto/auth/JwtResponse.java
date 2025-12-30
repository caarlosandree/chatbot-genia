package com.chatbotgen.aplicacao.dto.auth;

/**
 * DTO para resposta de autenticação contendo os tokens JWT.
 * 
 * @param accessToken Token de acesso JWT
 * @param refreshToken Token de renovação
 * @param tokenType Tipo do token (padrão: "Bearer")
 * @param expiresIn Tempo de expiração do access token em milissegundos
 * @author Sistema
 */
public record JwtResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {
    /**
     * Construtor com tokenType padrão.
     */
    public JwtResponse(String accessToken, String refreshToken, Long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }
}

