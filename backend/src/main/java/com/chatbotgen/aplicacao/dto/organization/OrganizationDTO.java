package com.chatbotgen.aplicacao.dto.organization;

import java.time.LocalDateTime;

/**
 * DTO para resposta de organização.
 * 
 * @param id Identificador da organização
 * @param name Nome da organização
 * @param document CNPJ ou CPF da organização
 * @param email Email de contato da organização
 * @param phone Telefone de contato da organização
 * @param active Indica se a organização está ativa
 * @param createdAt Data e hora de criação
 * @param updatedAt Data e hora da última atualização
 * @author Sistema
 */
public record OrganizationDTO(
    Long id,
    String name,
    String document,
    String email,
    String phone,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

