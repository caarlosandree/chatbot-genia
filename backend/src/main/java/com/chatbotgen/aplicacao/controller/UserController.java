package com.chatbotgen.aplicacao.controller;

import com.chatbotgen.aplicacao.dto.user.CreateUserRequest;
import com.chatbotgen.aplicacao.dto.user.UpdateUserRequest;
import com.chatbotgen.aplicacao.dto.user.UserResponse;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.service.UserService;
import com.chatbotgen.aplicacao.util.SecurityUtils;
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
 * Controller para operações de gerenciamento de usuários.
 * 
 * @author Sistema
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    /**
     * Lista todos os usuários (ADMIN vê todos, CLIENT vê apenas próprio).
     * 
     * @return Lista de usuários
     */
    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários (ADMIN vê todos, CLIENT vê apenas próprio)")
    public ResponseEntity<List<UserResponse>> getAll() {
        User authenticatedUser = securityUtils.getCurrentUser();
        List<UserResponse> users = userService.getAll(authenticatedUser);
        return ResponseEntity.ok(users);
    }

    /**
     * Busca um usuário por ID (ADMIN pode buscar qualquer, CLIENT apenas próprio).
     * 
     * @param id ID do usuário
     * @return UserResponse
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário", description = "Busca um usuário por ID (ADMIN pode buscar qualquer, CLIENT apenas próprio)")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        User authenticatedUser = securityUtils.getCurrentUser();
        UserResponse user = userService.getById(id, authenticatedUser);
        return ResponseEntity.ok(user);
    }

    /**
     * Cria um novo usuário (apenas ADMIN pode criar).
     * 
     * @param request Dados do usuário a ser criado
     * @return UserResponse do usuário criado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário (apenas ADMIN pode criar)")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        User authenticatedUser = securityUtils.getCurrentUser();
        UserResponse user = userService.create(request, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Atualiza um usuário (ADMIN pode atualizar qualquer, CLIENT apenas próprio).
     * 
     * @param id ID do usuário a ser atualizado
     * @param request Dados a serem atualizados
     * @return UserResponse do usuário atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário (ADMIN pode atualizar qualquer, CLIENT apenas próprio)")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        User authenticatedUser = securityUtils.getCurrentUser();
        UserResponse user = userService.update(id, request, authenticatedUser);
        return ResponseEntity.ok(user);
    }

    /**
     * Deleta um usuário (apenas ADMIN pode deletar).
     * 
     * @param id ID do usuário a ser deletado
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário (apenas ADMIN pode deletar)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User authenticatedUser = securityUtils.getCurrentUser();
        userService.delete(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}

