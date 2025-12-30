package com.chatbotgen.aplicacao.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de organização.
 * 
 * @param name Nome da organização
 * @param document CNPJ ou CPF da organização
 * @param email Email de contato da organização
 * @param phone Telefone de contato da organização
 * @author Sistema
 */
public record CreateOrganizationRequest(
    @NotBlank(message = "Nome da organização é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    String name,
    
    @NotBlank(message = "Documento (CNPJ/CPF) é obrigatório")
    @Size(min = 11, max = 20, message = "Documento deve ter entre 11 e 20 caracteres")
    String document,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    String phone
) {
}

