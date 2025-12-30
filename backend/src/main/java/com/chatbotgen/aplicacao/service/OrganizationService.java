package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.organization.CreateOrganizationRequest;
import com.chatbotgen.aplicacao.dto.organization.OrganizationDTO;
import com.chatbotgen.aplicacao.dto.organization.UpdateOrganizationRequest;
import com.chatbotgen.aplicacao.exception.OrganizationNotFoundException;
import com.chatbotgen.aplicacao.model.Organization;
import com.chatbotgen.aplicacao.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de negócio de organizações.
 * 
 * @author Sistema
 */
@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Cria uma nova organização.
     * 
     * @param request Dados da organização a ser criada
     * @return OrganizationDTO da organização criada
     */
    @Transactional
    public OrganizationDTO create(CreateOrganizationRequest request) {
        if (organizationRepository.existsByDocument(request.document())) {
            throw new RuntimeException("Documento já cadastrado");
        }

        if (organizationRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Organization organization = Organization.builder()
                .name(request.name())
                .document(request.document())
                .email(request.email())
                .phone(request.phone())
                .active(true)
                .build();

        Organization savedOrganization = organizationRepository.save(organization);
        return toDTO(savedOrganization);
    }

    /**
     * Busca uma organização por ID.
     * 
     * @param id ID da organização
     * @return OrganizationDTO
     * @throws OrganizationNotFoundException se a organização não for encontrada
     */
    public OrganizationDTO findById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organização não encontrada"));
        return toDTO(organization);
    }

    /**
     * Busca a entidade Organization por ID.
     * 
     * @param id ID da organização
     * @return Organization
     * @throws OrganizationNotFoundException se a organização não for encontrada
     */
    public Organization findEntityById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organização não encontrada"));
    }

    /**
     * Atualiza uma organização.
     * 
     * @param id ID da organização a ser atualizada
     * @param request Dados a serem atualizados
     * @return OrganizationDTO da organização atualizada
     * @throws OrganizationNotFoundException se a organização não for encontrada
     */
    @Transactional
    public OrganizationDTO update(Long id, UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organização não encontrada"));

        if (request.name() != null && !request.name().isBlank()) {
            organization.setName(request.name());
        }

        if (request.email() != null && !request.email().isBlank()) {
            if (organizationRepository.existsByEmail(request.email()) && 
                !organization.getEmail().equals(request.email())) {
                throw new RuntimeException("Email já cadastrado");
            }
            organization.setEmail(request.email());
        }

        if (request.phone() != null) {
            organization.setPhone(request.phone());
        }

        Organization updatedOrganization = organizationRepository.save(organization);
        return toDTO(updatedOrganization);
    }

    /**
     * Converte entidade Organization para DTO.
     * 
     * @param organization Entidade Organization
     * @return OrganizationDTO
     */
    private OrganizationDTO toDTO(Organization organization) {
        return new OrganizationDTO(
                organization.getId(),
                organization.getName(),
                organization.getDocument(),
                organization.getEmail(),
                organization.getPhone(),
                organization.getActive(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }
}

