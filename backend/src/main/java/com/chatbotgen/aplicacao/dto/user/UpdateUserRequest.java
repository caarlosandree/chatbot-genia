package com.chatbotgen.aplicacao.dto.user;

import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de atualização de usuário.
 * Todos os campos são opcionais (apenas os fornecidos serão atualizados).
 * 
 * @param name Nome do usuário
 * @param phone Telefone do usuário
 * @param active Indica se o usuário está ativo
 * @author Sistema
 */
public record UpdateUserRequest(
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    String name,
    
    String phone,
    
    Boolean active
) {
}

