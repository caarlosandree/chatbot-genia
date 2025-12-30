package com.chatbotgen.aplicacao.controller;

import com.chatbotgen.aplicacao.dto.plan.CreatePlanRequest;
import com.chatbotgen.aplicacao.dto.plan.PlanDTO;
import com.chatbotgen.aplicacao.dto.plan.UpdatePlanRequest;
import com.chatbotgen.aplicacao.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller para operações de planos.
 * 
 * @author Sistema
 */
@RestController
@RequestMapping("/api/v1/plans")
@Tag(name = "Planos", description = "Endpoints para visualização de planos")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    /**
     * Lista todos os planos ativos (público).
     * 
     * @return Lista de planos ativos
     */
    @GetMapping
    @Operation(summary = "Listar planos", description = "Lista todos os planos ativos disponíveis")
    public ResponseEntity<List<PlanDTO>> getAll() {
        List<PlanDTO> plans = planService.findAllActive();
        return ResponseEntity.ok(plans);
    }

    /**
     * Lista todos os planos, incluindo inativos (apenas ADMIN).
     * 
     * @return Lista de todos os planos
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Listar todos os planos",
        description = "Lista todos os planos, incluindo inativos (apenas ADMIN)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<PlanDTO>> getAllPlans() {
        List<PlanDTO> plans = planService.findAll();
        return ResponseEntity.ok(plans);
    }

    /**
     * Busca um plano por ID (público).
     * 
     * @param id ID do plano
     * @return PlanDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar plano", description = "Busca um plano por ID")
    public ResponseEntity<PlanDTO> getById(@PathVariable Long id) {
        PlanDTO plan = planService.findActiveById(id);
        return ResponseEntity.ok(plan);
    }

    /**
     * Cria um novo plano (apenas ADMIN).
     * 
     * @param request Dados do plano a ser criado
     * @return PlanDTO do plano criado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Criar plano",
        description = "Cria um novo plano (apenas ADMIN pode criar)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<PlanDTO> create(@Valid @RequestBody CreatePlanRequest request) {
        PlanDTO plan = planService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(plan);
    }

    /**
     * Atualiza um plano existente (apenas ADMIN).
     * 
     * @param id ID do plano a ser atualizado
     * @param request Dados a serem atualizados
     * @return PlanDTO do plano atualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Atualizar plano",
        description = "Atualiza um plano existente (apenas ADMIN pode atualizar)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<PlanDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlanRequest request) {
        PlanDTO plan = planService.update(id, request);
        return ResponseEntity.ok(plan);
    }

    /**
     * Deleta um plano (apenas ADMIN).
     * 
     * @param id ID do plano a ser deletado
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Deletar plano",
        description = "Deleta um plano (apenas ADMIN pode deletar)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

