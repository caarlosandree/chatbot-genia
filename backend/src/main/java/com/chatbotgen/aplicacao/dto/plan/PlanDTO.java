package com.chatbotgen.aplicacao.dto.plan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de plano.
 * 
 * @param id Identificador do plano
 * @param name Nome do plano
 * @param description Descrição do plano
 * @param price Preço mensal do plano
 * @param maxUsers Limite máximo de usuários (-1 para ilimitado)
 * @param maxChatbots Limite máximo de chatbots (-1 para ilimitado)
 * @param maxPhoneNumbers Limite máximo de números de telefone (-1 para ilimitado)
 * @param active Indica se o plano está ativo
 * @param createdAt Data e hora de criação
 * @param updatedAt Data e hora da última atualização
 * @author Sistema
 */
public record PlanDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer maxUsers,
    Integer maxChatbots,
    Integer maxPhoneNumbers,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

