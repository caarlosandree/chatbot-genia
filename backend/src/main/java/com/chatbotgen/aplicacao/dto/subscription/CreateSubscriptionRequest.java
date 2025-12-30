package com.chatbotgen.aplicacao.dto.subscription;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO para requisição de criação de assinatura.
 * 
 * @param organizationId Identificador da organização
 * @param planId Identificador do plano
 * @param startDate Data de início da assinatura
 * @param endDate Data de término da assinatura (opcional)
 * @author Sistema
 */
public record CreateSubscriptionRequest(
    @NotNull(message = "ID da organização é obrigatório")
    Long organizationId,
    
    @NotNull(message = "ID do plano é obrigatório")
    Long planId,
    
    @NotNull(message = "Data de início é obrigatória")
    LocalDateTime startDate,
    
    LocalDateTime endDate
) {
}

