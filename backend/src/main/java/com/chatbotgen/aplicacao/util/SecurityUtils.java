package com.chatbotgen.aplicacao.util;

import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.repository.UserRepository;
import com.chatbotgen.aplicacao.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilitário para operações relacionadas à segurança e autenticação.
 * 
 * @author Sistema
 */
@Component
public class SecurityUtils {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityUtils(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Obtém o usuário autenticado atual a partir do SecurityContext.
     * 
     * @return User autenticado
     * @throws RuntimeException se não houver usuário autenticado
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
     * Obtém o email do usuário autenticado a partir do SecurityContext.
     * 
     * @return Email do usuário autenticado
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        return authentication.getName();
    }
}

