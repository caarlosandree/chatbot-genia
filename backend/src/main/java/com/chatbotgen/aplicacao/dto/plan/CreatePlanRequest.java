package com.chatbotgen.aplicacao.dto.plan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação de plano.
 * 
 * @param name Nome do plano
 * @param description Descrição do plano
 * @param price Preço mensal do plano
 * @param maxUsers Limite máximo de usuários (-1 para ilimitado)
 * @param maxChatbots Limite máximo de chatbots (-1 para ilimitado)
 * @param maxPhoneNumbers Limite máximo de números de telefone (-1 para ilimitado)
 * @param active Indica se o plano está ativo (padrão: true)
 * @author Sistema
 */
public record CreatePlanRequest(
    @NotBlank(message = "Nome do plano é obrigatório")
    String name,
    
    String description,
    
    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser positivo ou zero")
    BigDecimal price,
    
    @NotNull(message = "Limite de usuários é obrigatório")
    Integer maxUsers,
    
    @NotNull(message = "Limite de chatbots é obrigatório")
    Integer maxChatbots,
    
    @NotNull(message = "Limite de números de telefone é obrigatório")
    Integer maxPhoneNumbers,
    
    Boolean active
) {
}

