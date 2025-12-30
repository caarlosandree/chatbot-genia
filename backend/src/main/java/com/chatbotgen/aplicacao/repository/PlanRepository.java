package com.chatbotgen.aplicacao.repository;

import com.chatbotgen.aplicacao.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de acesso a dados da entidade Plan.
 * 
 * @author Sistema
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    /**
     * Busca todos os planos ativos.
     * 
     * @return Lista de planos ativos
     */
    List<Plan> findByActiveTrue();

    /**
     * Busca um plano por ID e status ativo.
     * 
     * @param id ID do plano
     * @return Optional contendo o plano se encontrado e ativo
     */
    Optional<Plan> findByIdAndActiveTrue(Long id);
}

