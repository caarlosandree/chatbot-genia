package com.chatbotgen.aplicacao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração do banco de dados PostgreSQL e JPA.
 * 
 * @author Sistema
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.chatbotgen.aplicacao.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // Configuração do banco de dados é feita via application.properties
    // Esta classe habilita funcionalidades adicionais do JPA:
    // - @EnableJpaRepositories: habilita repositórios JPA
    // - @EnableJpaAuditing: habilita auditoria automática (@CreatedDate, @LastModifiedDate, etc.)
    // - @EnableTransactionManagement: habilita gerenciamento de transações
}

