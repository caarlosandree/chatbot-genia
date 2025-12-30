package com.chatbotgen.aplicacao.controller;

import com.chatbotgen.aplicacao.dto.subscription.CreateSubscriptionRequest;
import com.chatbotgen.aplicacao.dto.subscription.PlanLimitsResponse;
import com.chatbotgen.aplicacao.dto.subscription.SubscriptionDTO;
import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.service.PlanLimitService;
import com.chatbotgen.aplicacao.service.SubscriptionService;
import com.chatbotgen.aplicacao.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para operações de assinaturas.
 * 
 * @author Sistema
 */
@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Assinaturas", description = "Endpoints para gerenciamento de assinaturas")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PlanLimitService planLimitService;
    private final SecurityUtils securityUtils;

    public SubscriptionController(
            SubscriptionService subscriptionService,
            PlanLimitService planLimitService,
            SecurityUtils securityUtils) {
        this.subscriptionService = subscriptionService;
        this.planLimitService = planLimitService;
        this.securityUtils = securityUtils;
    }

    /**
     * Cria uma nova assinatura (admin ou próprio).
     * 
     * @param request Dados da assinatura a ser criada
     * @return SubscriptionDTO da assinatura criada
     */
    @PostMapping
    @Operation(summary = "Criar assinatura", description = "Cria uma nova assinatura (admin ou próprio)")
    public ResponseEntity<SubscriptionDTO> create(@Valid @RequestBody CreateSubscriptionRequest request) {
        User authenticatedUser = securityUtils.getCurrentUser();
        
        // Verifica se é admin ou se está criando para sua própria organização
        if (authenticatedUser.getRole() != Role.ADMIN) {
            if (authenticatedUser.getOrganization() == null || 
                !authenticatedUser.getOrganization().getId().equals(request.organizationId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        SubscriptionDTO subscription = subscriptionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    /**
     * Retorna a assinatura ativa do usuário atual.
     * 
     * @return SubscriptionDTO
     */
    @GetMapping("/me")
    @Operation(summary = "Minha assinatura", description = "Retorna a assinatura ativa do usuário atual")
    public ResponseEntity<SubscriptionDTO> getMySubscription() {
        User authenticatedUser = securityUtils.getCurrentUser();
        
        if (authenticatedUser.getOrganization() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SubscriptionDTO subscription = subscriptionService.findActiveByOrganization(
                authenticatedUser.getOrganization().getId());
        return ResponseEntity.ok(subscription);
    }

    /**
     * Busca uma assinatura por ID.
     * 
     * @param id ID da assinatura
     * @return SubscriptionDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar assinatura", description = "Busca uma assinatura por ID")
    public ResponseEntity<SubscriptionDTO> getById(@PathVariable Long id) {
        User authenticatedUser = securityUtils.getCurrentUser();
        SubscriptionDTO subscription = subscriptionService.findById(id);

        // Verifica se é admin ou se a assinatura é da sua organização
        if (authenticatedUser.getRole() != Role.ADMIN) {
            if (authenticatedUser.getOrganization() == null || 
                !authenticatedUser.getOrganization().getId().equals(subscription.organization().id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(subscription);
    }

    /**
     * Cancela uma assinatura.
     * 
     * @param id ID da assinatura
     * @return SubscriptionDTO da assinatura cancelada
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancelar assinatura", description = "Cancela uma assinatura")
    public ResponseEntity<SubscriptionDTO> cancel(@PathVariable Long id) {
        User authenticatedUser = securityUtils.getCurrentUser();
        SubscriptionDTO subscription = subscriptionService.findById(id);

        // Verifica se é admin ou se a assinatura é da sua organização
        if (authenticatedUser.getRole() != Role.ADMIN) {
            if (authenticatedUser.getOrganization() == null || 
                !authenticatedUser.getOrganization().getId().equals(subscription.organization().id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        SubscriptionDTO cancelledSubscription = subscriptionService.cancel(id);
        return ResponseEntity.ok(cancelledSubscription);
    }

    /**
     * Retorna os limites atuais do plano do usuário.
     * 
     * @return PlanLimitsResponse
     */
    @GetMapping("/limits")
    @Operation(summary = "Limites do plano", description = "Retorna os limites atuais do plano do usuário")
    public ResponseEntity<PlanLimitsResponse> getLimits() {
        User authenticatedUser = securityUtils.getCurrentUser();
        
        if (authenticatedUser.getOrganization() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Organization organization = authenticatedUser.getOrganization();
        PlanLimitsResponse limits = planLimitService.getCurrentLimits(organization);
        return ResponseEntity.ok(limits);
    }
}

