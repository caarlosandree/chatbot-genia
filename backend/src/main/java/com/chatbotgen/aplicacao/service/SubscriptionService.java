package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.organization.OrganizationDTO;
import com.chatbotgen.aplicacao.dto.plan.PlanDTO;
import com.chatbotgen.aplicacao.dto.subscription.CreateSubscriptionRequest;
import com.chatbotgen.aplicacao.dto.subscription.SubscriptionDTO;
import com.chatbotgen.aplicacao.exception.NoActiveSubscriptionException;
import com.chatbotgen.aplicacao.exception.OrganizationNotFoundException;
import com.chatbotgen.aplicacao.exception.PlanNotFoundException;
import com.chatbotgen.aplicacao.exception.SubscriptionNotFoundException;
import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.model.Plan;
import com.chatbotgen.aplicacao.model.Subscription;
import com.chatbotgen.aplicacao.model.SubscriptionStatus;
import com.chatbotgen.aplicacao.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio de assinaturas.
 * 
 * @author Sistema
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final OrganizationService organizationService;
    private final PlanService planService;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            OrganizationService organizationService,
            PlanService planService) {
        this.subscriptionRepository = subscriptionRepository;
        this.organizationService = organizationService;
        this.planService = planService;
    }

    /**
     * Cria uma nova assinatura (ativa nova, desativa anteriores).
     * 
     * @param request Dados da assinatura a ser criada
     * @return SubscriptionDTO da assinatura criada
     */
    @Transactional
    public SubscriptionDTO create(CreateSubscriptionRequest request) {
        Organization organization = organizationService.findEntityById(request.organizationId());
        Plan plan = planService.findEntityById(request.planId());

        // Desativa assinaturas anteriores da organização
        List<Subscription> activeSubscriptions = subscriptionRepository.findByOrganizationAndStatus(
                organization, SubscriptionStatus.ACTIVE);
        for (Subscription subscription : activeSubscriptions) {
            subscription.setStatus(SubscriptionStatus.INACTIVE);
            subscriptionRepository.save(subscription);
        }

        // Cria nova assinatura
        Subscription subscription = Subscription.builder()
                .organization(organization)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return toDTO(savedSubscription);
    }

    /**
     * Busca a assinatura ativa de uma organização.
     * 
     * @param organizationId ID da organização
     * @return SubscriptionDTO
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     */
    public SubscriptionDTO findActiveByOrganization(Long organizationId) {
        Organization organization = organizationService.findEntityById(organizationId);
        
        Subscription subscription = subscriptionRepository.findActiveByOrganization(
                organization, LocalDateTime.now())
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "Organização não possui assinatura ativa"));

        return toDTO(subscription);
    }

    /**
     * Busca uma assinatura por ID.
     * 
     * @param id ID da assinatura
     * @return SubscriptionDTO
     * @throws SubscriptionNotFoundException se a assinatura não for encontrada
     */
    public SubscriptionDTO findById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Assinatura não encontrada"));
        return toDTO(subscription);
    }

    /**
     * Cancela uma assinatura.
     * 
     * @param id ID da assinatura
     * @return SubscriptionDTO da assinatura cancelada
     * @throws SubscriptionNotFoundException se a assinatura não for encontrada
     */
    @Transactional
    public SubscriptionDTO cancel(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Assinatura não encontrada"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return toDTO(savedSubscription);
    }

    /**
     * Renova uma assinatura (cria nova com base na atual).
     * 
     * @param id ID da assinatura atual
     * @param newEndDate Nova data de término
     * @return SubscriptionDTO da assinatura renovada
     * @throws SubscriptionNotFoundException se a assinatura não for encontrada
     */
    @Transactional
    public SubscriptionDTO renew(Long id, LocalDateTime newEndDate) {
        Subscription currentSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Assinatura não encontrada"));

        // Desativa assinatura atual
        currentSubscription.setStatus(SubscriptionStatus.INACTIVE);
        subscriptionRepository.save(currentSubscription);

        // Cria nova assinatura
        Subscription newSubscription = Subscription.builder()
                .organization(currentSubscription.getOrganization())
                .plan(currentSubscription.getPlan())
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .endDate(newEndDate)
                .build();

        Subscription savedSubscription = subscriptionRepository.save(newSubscription);
        return toDTO(savedSubscription);
    }

    /**
     * Converte entidade Subscription para DTO.
     * 
     * @param subscription Entidade Subscription
     * @return SubscriptionDTO
     */
    private SubscriptionDTO toDTO(Subscription subscription) {
        OrganizationDTO organizationDTO = organizationService.findById(
                subscription.getOrganization().getId());
        PlanDTO planDTO = planService.findById(subscription.getPlan().getId());

        return new SubscriptionDTO(
                subscription.getId(),
                organizationDTO,
                planDTO,
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );
    }
}

