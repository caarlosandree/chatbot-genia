package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.subscription.PlanLimitsResponse;
import com.chatbotgen.aplicacao.exception.NoActiveSubscriptionException;
import com.chatbotgen.aplicacao.exception.PlanLimitExceededException;
import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.model.Plan;
import com.chatbotgen.aplicacao.model.Subscription;
import com.chatbotgen.aplicacao.repository.SubscriptionRepository;
import com.chatbotgen.aplicacao.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Serviço responsável por validar limites de planos.
 * 
 * @author Sistema
 */
@Service
@Transactional(readOnly = true)
public class PlanLimitService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public PlanLimitService(
            SubscriptionRepository subscriptionRepository,
            UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Valida o limite de usuários antes de criar um novo usuário.
     * 
     * @param organization Organização
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     * @throws PlanLimitExceededException se o limite for ultrapassado
     */
    public void checkUserLimit(Organization organization) {
        Plan plan = getActivePlan(organization);
        
        if (plan.isUnlimitedUsers()) {
            return; // Ilimitado
        }

        long currentUsers = userRepository.countByOrganizationId(organization.getId());

        if (currentUsers >= plan.getMaxUsers()) {
            throw new PlanLimitExceededException(
                    String.format("Limite de usuários (%d) do plano foi atingido", plan.getMaxUsers()));
        }
    }

    /**
     * Valida o limite de chatbots antes de criar um novo chatbot.
     * 
     * @param organization Organização
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     * @throws PlanLimitExceededException se o limite for ultrapassado
     */
    public void checkChatbotLimit(Organization organization) {
        Plan plan = getActivePlan(organization);
        
        if (plan.isUnlimitedChatbots()) {
            return; // Ilimitado
        }

        // TODO: Implementar quando criar entidade Chatbot
        // long currentChatbots = chatbotRepository.countByOrganization(organization);
        // if (currentChatbots >= plan.getMaxChatbots()) {
        //     throw new PlanLimitExceededException(
        //             String.format("Limite de chatbots (%d) do plano foi atingido", plan.getMaxChatbots()));
        // }
    }

    /**
     * Valida o limite de números de telefone antes de cadastrar um novo número.
     * 
     * @param organization Organização
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     * @throws PlanLimitExceededException se o limite for ultrapassado
     */
    public void checkPhoneNumberLimit(Organization organization) {
        Plan plan = getActivePlan(organization);
        
        if (plan.isUnlimitedPhoneNumbers()) {
            return; // Ilimitado
        }

        // TODO: Implementar quando criar entidade PhoneNumber
        // long currentPhoneNumbers = phoneNumberRepository.countByOrganization(organization);
        // if (currentPhoneNumbers >= plan.getMaxPhoneNumbers()) {
        //     throw new PlanLimitExceededException(
        //             String.format("Limite de números de telefone (%d) do plano foi atingido", 
        //                     plan.getMaxPhoneNumbers()));
        // }
    }

    /**
     * Retorna os limites atuais do plano da organização.
     * 
     * @param organization Organização
     * @return PlanLimitsResponse com limites e uso atual
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     */
    public PlanLimitsResponse getCurrentLimits(Organization organization) {
        Plan plan = getActivePlan(organization);

        long currentUsers = userRepository.countByOrganizationId(organization.getId());
        long currentChatbots = 0; // TODO: Implementar quando criar entidade Chatbot
        long currentPhoneNumbers = 0; // TODO: Implementar quando criar entidade PhoneNumber

        return new PlanLimitsResponse(
                plan.getMaxUsers(),
                plan.getMaxChatbots(),
                plan.getMaxPhoneNumbers(),
                currentUsers,
                currentChatbots,
                currentPhoneNumbers
        );
    }

    /**
     * Busca o plano ativo da organização.
     * 
     * @param organization Organização
     * @return Plan ativo
     * @throws NoActiveSubscriptionException se não houver assinatura ativa
     */
    private Plan getActivePlan(Organization organization) {
        Subscription subscription = subscriptionRepository.findActiveByOrganization(
                organization, LocalDateTime.now())
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "Organização não possui assinatura ativa"));

        return subscription.getPlan();
    }
}

