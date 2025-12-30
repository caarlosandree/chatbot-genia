package com.chatbotgen.aplicacao.dto.plan;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO para requisição de atualização de plano.
 * 
 * @param name Nome do plano
 * @param description Descrição do plano
 * @param price Preço mensal do plano
 * @param maxUsers Limite máximo de usuários (-1 para ilimitado)
 * @param maxChatbots Limite máximo de chatbots (-1 para ilimitado)
 * @param maxPhoneNumbers Limite máximo de números de telefone (-1 para ilimitado)
 * @param active Indica se o plano está ativo
 * @author Sistema
 */
public record UpdatePlanRequest(
    String name,
    
    String description,
    
    @PositiveOrZero(message = "Preço deve ser positivo ou zero")
    BigDecimal price,
    
    Integer maxUsers,
    
    Integer maxChatbots,
    
    Integer maxPhoneNumbers,
    
    Boolean active
) {
}

