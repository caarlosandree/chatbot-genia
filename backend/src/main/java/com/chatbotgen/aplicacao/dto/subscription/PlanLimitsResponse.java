package com.chatbotgen.aplicacao.dto.subscription;

/**
 * DTO para resposta com limites atuais do plano.
 * 
 * @param maxUsers Limite máximo de usuários (-1 para ilimitado)
 * @param maxChatbots Limite máximo de chatbots (-1 para ilimitado)
 * @param maxPhoneNumbers Limite máximo de números de telefone (-1 para ilimitado)
 * @param currentUsers Quantidade atual de usuários
 * @param currentChatbots Quantidade atual de chatbots
 * @param currentPhoneNumbers Quantidade atual de números de telefone
 * @author Sistema
 */
public record PlanLimitsResponse(
    Integer maxUsers,
    Integer maxChatbots,
    Integer maxPhoneNumbers,
    Long currentUsers,
    Long currentChatbots,
    Long currentPhoneNumbers
) {
}

