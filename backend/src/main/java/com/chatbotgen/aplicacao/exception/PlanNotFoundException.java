package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando um plano não é encontrado.
 * 
 * @author Sistema
 */
public class PlanNotFoundException extends RuntimeException {

    public PlanNotFoundException(String message) {
        super(message);
    }

    public PlanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

