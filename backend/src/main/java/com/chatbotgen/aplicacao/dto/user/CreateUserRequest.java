package com.chatbotgen.aplicacao.dto.user;

import com.chatbotgen.aplicacao.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de usuário.
 * 
 * @param email Email do usuário
 * @param password Senha do usuário
 * @param name Nome do usuário
 * @param phone Telefone do usuário
 * @param role Role do usuário
 * @author Sistema
 */
public record CreateUserRequest(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    String password,
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    String name,
    
    String phone,
    
    @NotNull(message = "Role é obrigatório")
    Role role
) {
}

