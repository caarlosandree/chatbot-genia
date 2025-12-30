package com.chatbotgen.aplicacao;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * 
 * @author Sistema
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Carrega variáveis de ambiente do arquivo .env.local se existir
        loadDotEnv();
        
        SpringApplication.run(Application.class, args);
    }

    /**
     * Carrega variáveis de ambiente do arquivo .env.local na raiz do projeto backend.
     * As variáveis são carregadas no System.getProperty() para serem usadas pelo Spring Boot.
     * O Spring Boot resolve ${VAR_NAME} primeiro de System.getenv(), depois de System.getProperty(),
     * e por último do arquivo de propriedades.
     */
    private static void loadDotEnv() {
        try {
            // Tenta carregar o arquivo .env.local na raiz do projeto backend
            String projectRoot = System.getProperty("user.dir");
            
            Dotenv dotenv = Dotenv.configure()
                    .directory(projectRoot)
                    .filename(".env.local")
                    .ignoreIfMissing()
                    .load();
            
            // Carrega as variáveis no System.getProperty() para o Spring Boot usar
            // O Spring Boot lê de System.getProperty() quando resolve ${VAR_NAME}
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                // Só define se ainda não foi definido (permite override por variáveis de ambiente do sistema)
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            });
        } catch (Exception e) {
            // Se o arquivo não existir, ignora silenciosamente
            // As variáveis de ambiente do sistema serão usadas como fallback
        }
    }
}

