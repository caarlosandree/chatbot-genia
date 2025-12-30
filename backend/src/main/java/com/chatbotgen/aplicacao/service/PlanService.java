package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.plan.CreatePlanRequest;
import com.chatbotgen.aplicacao.dto.plan.PlanDTO;
import com.chatbotgen.aplicacao.dto.plan.UpdatePlanRequest;
import com.chatbotgen.aplicacao.exception.PlanNotFoundException;
import com.chatbotgen.aplicacao.model.Plan;
import com.chatbotgen.aplicacao.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela lógica de negócio de planos.
 * 
 * @author Sistema
 */
@Service
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * Lista todos os planos (ativos e inativos) - para admin.
     * 
     * @return Lista de todos os planos
     */
    public List<PlanDTO> findAll() {
        return planRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Lista todos os planos ativos.
     * 
     * @return Lista de planos ativos
     */
    public List<PlanDTO> findAllActive() {
        return planRepository.findByActiveTrue().stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Busca um plano por ID.
     * 
     * @param id ID do plano
     * @return PlanDTO
     * @throws PlanNotFoundException se o plano não for encontrado
     */
    public PlanDTO findById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plano não encontrado"));
        return toDTO(plan);
    }

    /**
     * Busca um plano ativo por ID.
     * 
     * @param id ID do plano
     * @return PlanDTO
     * @throws PlanNotFoundException se o plano não for encontrado ou não estiver ativo
     */
    public PlanDTO findActiveById(Long id) {
        Plan plan = planRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PlanNotFoundException("Plano não encontrado ou inativo"));
        return toDTO(plan);
    }

    /**
     * Busca a entidade Plan por ID.
     * 
     * @param id ID do plano
     * @return Plan
     * @throws PlanNotFoundException se o plano não for encontrado
     */
    public Plan findEntityById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plano não encontrado"));
    }

    /**
     * Cria um novo plano.
     * 
     * @param request Dados do plano a ser criado
     * @return PlanDTO do plano criado
     */
    @Transactional
    public PlanDTO create(CreatePlanRequest request) {
        Plan plan = Plan.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .maxUsers(request.maxUsers())
                .maxChatbots(request.maxChatbots())
                .maxPhoneNumbers(request.maxPhoneNumbers())
                .active(request.active() != null ? request.active() : true)
                .build();

        Plan savedPlan = planRepository.save(plan);
        return toDTO(savedPlan);
    }

    /**
     * Atualiza um plano existente.
     * 
     * @param id ID do plano a ser atualizado
     * @param request Dados a serem atualizados
     * @return PlanDTO do plano atualizado
     * @throws PlanNotFoundException se o plano não for encontrado
     */
    @Transactional
    public PlanDTO update(Long id, UpdatePlanRequest request) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plano não encontrado"));

        if (request.name() != null) {
            plan.setName(request.name());
        }
        if (request.description() != null) {
            plan.setDescription(request.description());
        }
        if (request.price() != null) {
            plan.setPrice(request.price());
        }
        if (request.maxUsers() != null) {
            plan.setMaxUsers(request.maxUsers());
        }
        if (request.maxChatbots() != null) {
            plan.setMaxChatbots(request.maxChatbots());
        }
        if (request.maxPhoneNumbers() != null) {
            plan.setMaxPhoneNumbers(request.maxPhoneNumbers());
        }
        if (request.active() != null) {
            plan.setActive(request.active());
        }

        Plan updatedPlan = planRepository.save(plan);
        return toDTO(updatedPlan);
    }

    /**
     * Deleta um plano.
     * 
     * @param id ID do plano a ser deletado
     * @throws PlanNotFoundException se o plano não for encontrado
     */
    @Transactional
    public void delete(Long id) {
        if (!planRepository.existsById(id)) {
            throw new PlanNotFoundException("Plano não encontrado");
        }

        planRepository.deleteById(id);
    }

    /**
     * Converte entidade Plan para DTO.
     * 
     * @param plan Entidade Plan
     * @return PlanDTO
     */
    private PlanDTO toDTO(Plan plan) {
        return new PlanDTO(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getMaxUsers(),
                plan.getMaxChatbots(),
                plan.getMaxPhoneNumbers(),
                plan.getActive(),
                plan.getCreatedAt(),
                plan.getUpdatedAt()
        );
    }
}

