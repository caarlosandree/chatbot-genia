package com.chatbotgen.aplicacao.controller;

import com.chatbotgen.aplicacao.dto.organization.CreateOrganizationRequest;
import com.chatbotgen.aplicacao.dto.organization.OrganizationDTO;
import com.chatbotgen.aplicacao.dto.organization.UpdateOrganizationRequest;
import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.service.OrganizationService;
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
 * Controller para operações de organizações.
 * 
 * @author Sistema
 */
@RestController
@RequestMapping("/api/v1/organizations")
@Tag(name = "Organizações", description = "Endpoints para gerenciamento de organizações")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final SecurityUtils securityUtils;

    public OrganizationController(OrganizationService organizationService, SecurityUtils securityUtils) {
        this.organizationService = organizationService;
        this.securityUtils = securityUtils;
    }

    /**
     * Cria uma nova organização (público para registro, ou admin).
     * 
     * @param request Dados da organização a ser criada
     * @return OrganizationDTO da organização criada
     */
    @PostMapping
    @Operation(summary = "Criar organização", description = "Cria uma nova organização")
    public ResponseEntity<OrganizationDTO> create(@Valid @RequestBody CreateOrganizationRequest request) {
        OrganizationDTO organization = organizationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(organization);
    }

    /**
     * Busca uma organização por ID (própria ou admin).
     * 
     * @param id ID da organização
     * @return OrganizationDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar organização", description = "Busca uma organização por ID (própria ou admin)")
    public ResponseEntity<OrganizationDTO> getById(@PathVariable Long id) {
        User authenticatedUser = securityUtils.getCurrentUser();
        OrganizationDTO organization = organizationService.findById(id);

        // Verifica se é a própria organização ou admin
        if (authenticatedUser.getRole() != Role.ADMIN && 
            (authenticatedUser.getOrganization() == null || 
             !authenticatedUser.getOrganization().getId().equals(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(organization);
    }

    /**
     * Retorna a organização do usuário atual.
     * 
     * @return OrganizationDTO
     */
    @GetMapping("/me")
    @Operation(summary = "Minha organização", description = "Retorna a organização do usuário atual")
    public ResponseEntity<OrganizationDTO> getMyOrganization() {
        User authenticatedUser = securityUtils.getCurrentUser();
        
        if (authenticatedUser.getOrganization() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        OrganizationDTO organization = organizationService.findById(
                authenticatedUser.getOrganization().getId());
        return ResponseEntity.ok(organization);
    }

    /**
     * Atualiza uma organização (própria ou admin).
     * 
     * @param id ID da organização a ser atualizada
     * @param request Dados a serem atualizados
     * @return OrganizationDTO da organização atualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar organização", description = "Atualiza uma organização (própria ou admin)")
    public ResponseEntity<OrganizationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganizationRequest request) {
        User authenticatedUser = securityUtils.getCurrentUser();
        OrganizationDTO organization = organizationService.update(id, request);

        // Verifica se é a própria organização ou admin
        if (authenticatedUser.getRole() != Role.ADMIN && 
            (authenticatedUser.getOrganization() == null || 
             !authenticatedUser.getOrganization().getId().equals(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(organization);
    }
}

