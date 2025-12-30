package com.chatbotgen.aplicacao.config;

import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração para inicializar o usuário admin se não existir.
 * 
 * @author Sistema
 */
@Configuration
public class AdminInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);
    private static final String ADMIN_EMAIL = "caarlosandree@gmail.com";
    private static final String ADMIN_PASSWORD = "ThaysCF@0308";
    private static final String ADMIN_NAME = "Administrador";

    @Bean
    public ApplicationRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                logger.info("Criando usuário administrador inicial...");
                
                User admin = User.builder()
                        .email(ADMIN_EMAIL)
                        .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                        .name(ADMIN_NAME)
                        .role(Role.ADMIN)
                        .active(true)
                        .build();

                userRepository.save(admin);
                logger.info("Usuário administrador criado com sucesso: {}", ADMIN_EMAIL);
            } else {
                logger.debug("Usuário administrador já existe: {}", ADMIN_EMAIL);
            }
        };
    }
}

