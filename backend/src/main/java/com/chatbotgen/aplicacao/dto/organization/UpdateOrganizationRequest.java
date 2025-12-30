package com.chatbotgen.aplicacao.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de atualização de organização.
 * 
 * @param name Nome da organização
 * @param email Email de contato da organização
 * @param phone Telefone de contato da organização
 * @author Sistema
 */
public record UpdateOrganizationRequest(
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    String name,
    
    @Email(message = "Email deve ser válido")
    String email,
    
    String phone
) {
}

