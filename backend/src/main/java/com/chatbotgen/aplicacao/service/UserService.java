package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.user.CreateUserRequest;
import com.chatbotgen.aplicacao.dto.user.UpdateUserRequest;
import com.chatbotgen.aplicacao.dto.user.UserMapper;
import com.chatbotgen.aplicacao.dto.user.UserResponse;
import com.chatbotgen.aplicacao.exception.EmailAlreadyExistsException;
import com.chatbotgen.aplicacao.exception.ForbiddenException;
import com.chatbotgen.aplicacao.exception.UserNotFoundException;
import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.repository.UserRepository;
import com.chatbotgen.aplicacao.service.PlanLimitService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela lógica de negócio de usuários.
 * 
 * @author Sistema
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PlanLimitService planLimitService;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            PlanLimitService planLimitService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.planLimitService = planLimitService;
    }

    /**
     * Lista todos os usuários (ADMIN vê todos, CLIENT vê apenas próprio).
     * 
     * @param authenticatedUser Usuário autenticado
     * @return Lista de usuários
     */
    public List<UserResponse> getAll(User authenticatedUser) {
        if (authenticatedUser.getRole() == Role.ADMIN) {
            return userRepository.findAll().stream()
                    .map(userMapper::toResponse)
                    .toList();
        } else {
            return List.of(userMapper.toResponse(authenticatedUser));
        }
    }

    /**
     * Busca um usuário por ID (ADMIN pode buscar qualquer, CLIENT apenas próprio).
     * 
     * @param id ID do usuário
     * @param authenticatedUser Usuário autenticado
     * @return UserResponse
     * @throws UserNotFoundException se o usuário não for encontrado
     * @throws ForbiddenException se CLIENT tentar buscar outro usuário
     */
    public UserResponse getById(Long id, User authenticatedUser) {
        if (authenticatedUser.getRole() != Role.ADMIN && !authenticatedUser.getId().equals(id)) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return userMapper.toResponse(user);
    }

    /**
     * Cria um novo usuário (apenas ADMIN pode criar).
     * 
     * @param request Dados do usuário a ser criado
     * @param authenticatedUser Usuário autenticado
     * @return UserResponse do usuário criado
     * @throws ForbiddenException se CLIENT tentar criar usuário
     * @throws EmailAlreadyExistsException se o email já existir
     */
    @Transactional
    public UserResponse create(CreateUserRequest request, User authenticatedUser) {
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Apenas administradores podem criar usuários");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setActive(true);

        // Se o usuário tem organização, valida limite antes de criar
        if (user.getOrganization() != null) {
            planLimitService.checkUserLimit(user.getOrganization());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Atualiza um usuário (ADMIN pode atualizar qualquer, CLIENT apenas próprio).
     * 
     * @param id ID do usuário a ser atualizado
     * @param request Dados a serem atualizados
     * @param authenticatedUser Usuário autenticado
     * @return UserResponse do usuário atualizado
     * @throws UserNotFoundException se o usuário não for encontrado
     * @throws ForbiddenException se CLIENT tentar atualizar outro usuário
     */
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request, User authenticatedUser) {
        if (authenticatedUser.getRole() != Role.ADMIN && !authenticatedUser.getId().equals(id)) {
            throw new ForbiddenException("Você não tem permissão para atualizar este usuário");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        userMapper.updateEntity(request, user);
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    /**
     * Deleta um usuário (apenas ADMIN pode deletar).
     * 
     * @param id ID do usuário a ser deletado
     * @param authenticatedUser Usuário autenticado
     * @throws ForbiddenException se CLIENT tentar deletar
     * @throws UserNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public void delete(Long id, User authenticatedUser) {
        if (authenticatedUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Apenas administradores podem deletar usuários");
        }

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }
}

