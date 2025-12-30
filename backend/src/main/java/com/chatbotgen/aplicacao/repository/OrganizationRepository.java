package com.chatbotgen.aplicacao.repository;

import com.chatbotgen.aplicacao.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações de acesso a dados da entidade Organization.
 * 
 * @author Sistema
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /**
     * Busca uma organização pelo documento (CNPJ/CPF).
     * 
     * @param document Documento da organização
     * @return Optional contendo a organização se encontrada
     */
    Optional<Organization> findByDocument(String document);

    /**
     * Busca uma organização pelo email.
     * 
     * @param email Email da organização
     * @return Optional contendo a organização se encontrada
     */
    Optional<Organization> findByEmail(String email);

    /**
     * Verifica se existe uma organização com o documento especificado.
     * 
     * @param document Documento a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByDocument(String document);

    /**
     * Verifica se existe uma organização com o email especificado.
     * 
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
}

