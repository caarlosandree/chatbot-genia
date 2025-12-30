package com.chatbotgen.aplicacao.model;

/**
 * Enum que representa o status de uma assinatura.
 * 
 * @author Sistema
 */
public enum SubscriptionStatus {
    /**
     * Assinatura ativa e em uso.
     */
    ACTIVE,
    
    /**
     * Assinatura inativa (não está sendo usada no momento).
     */
    INACTIVE,
    
    /**
     * Assinatura expirada (data de término passou).
     */
    EXPIRED,
    
    /**
     * Assinatura cancelada.
     */
    CANCELLED
}

