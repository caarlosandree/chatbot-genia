package com.chatbotgen.aplicacao.dto.user;

import com.chatbotgen.aplicacao.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper MapStruct para conversão entre User entity e DTOs.
 * 
 * @author Sistema
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte User entity para UserResponse DTO.
     * 
     * @param user Entidade User
     * @return UserResponse DTO
     */
    UserResponse toResponse(User user);

    /**
     * Converte CreateUserRequest DTO para User entity.
     * Não inclui passwordHash pois deve ser feito manualmente no service.
     * 
     * @param request CreateUserRequest DTO
     * @return User entity (sem passwordHash)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "organization", ignore = true)
    User toEntity(CreateUserRequest request);

    /**
     * Atualiza User entity com dados de UpdateUserRequest.
     * Apenas campos fornecidos (não nulos) são atualizados.
     * 
     * @param request UpdateUserRequest DTO
     * @param user Entidade User a ser atualizada
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phone", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "active", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}

