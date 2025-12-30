package com.chatbotgen.aplicacao.repository;

import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de acesso a dados da entidade User.
 * 
 * @author Sistema
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca todos os usuários por role.
     * 
     * @param role Role dos usuários
     * @return Lista de usuários com a role especificada
     */
    List<User> findByRole(Role role);

    /**
     * Verifica se existe um usuário com o email especificado.
     * 
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Conta o número de usuários de uma organização.
     * 
     * @param organizationId ID da organização
     * @return Número de usuários da organização
     */
    long countByOrganizationId(Long organizationId);
}

