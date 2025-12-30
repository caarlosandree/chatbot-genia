package com.chatbotgen.aplicacao.dto.user;

import com.chatbotgen.aplicacao.model.Role;

import java.time.LocalDateTime;

/**
 * DTO para resposta de usuário (sem informações sensíveis).
 * 
 * @param id Identificador do usuário
 * @param email Email do usuário
 * @param name Nome do usuário
 * @param phone Telefone do usuário
 * @param role Role do usuário
 * @param active Indica se o usuário está ativo
 * @param lastLogin Data e hora do último login
 * @param createdAt Data e hora de criação
 * @param updatedAt Data e hora da última atualização
 * @author Sistema
 */
public record UserResponse(
    Long id,
    String email,
    String name,
    String phone,
    Role role,
    Boolean active,
    LocalDateTime lastLogin,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

