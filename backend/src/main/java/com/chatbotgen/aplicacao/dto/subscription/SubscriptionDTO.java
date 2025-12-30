package com.chatbotgen.aplicacao.dto.subscription;

import com.chatbotgen.aplicacao.dto.organization.OrganizationDTO;
import com.chatbotgen.aplicacao.dto.plan.PlanDTO;
import com.chatbotgen.aplicacao.model.SubscriptionStatus;

import java.time.LocalDateTime;

/**
 * DTO para resposta de assinatura.
 * 
 * @param id Identificador da assinatura
 * @param organization Organização da assinatura
 * @param plan Plano da assinatura
 * @param status Status da assinatura
 * @param startDate Data de início da assinatura
 * @param endDate Data de término da assinatura
 * @param createdAt Data e hora de criação
 * @param updatedAt Data e hora da última atualização
 * @author Sistema
 */
public record SubscriptionDTO(
    Long id,
    OrganizationDTO organization,
    PlanDTO plan,
    SubscriptionStatus status,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

