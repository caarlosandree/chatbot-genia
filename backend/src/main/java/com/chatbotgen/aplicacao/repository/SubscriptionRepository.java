package com.chatbotgen.aplicacao.repository;

import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.model.Subscription;
import com.chatbotgen.aplicacao.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de acesso a dados da entidade Subscription.
 * 
 * @author Sistema
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Busca a assinatura ativa de uma organização.
     * 
     * @param organization Organização
     * @return Optional contendo a assinatura ativa se encontrada
     */
    @Query("SELECT s FROM Subscription s WHERE s.organization = :organization " +
           "AND s.status = 'ACTIVE' " +
           "AND (s.startDate IS NULL OR s.startDate <= :now) " +
           "AND (s.endDate IS NULL OR s.endDate >= :now) " +
           "ORDER BY s.startDate DESC")
    Optional<Subscription> findActiveByOrganization(
            @Param("organization") Organization organization,
            @Param("now") LocalDateTime now
    );

    /**
     * Busca assinaturas de uma organização por status.
     * 
     * @param organization Organização
     * @param status Status da assinatura
     * @return Lista de assinaturas com o status especificado
     */
    List<Subscription> findByOrganizationAndStatus(Organization organization, SubscriptionStatus status);

    /**
     * Busca todas as assinaturas de uma organização.
     * 
     * @param organization Organização
     * @return Lista de assinaturas da organização
     */
    List<Subscription> findByOrganization(Organization organization);
}

