package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando um limite do plano é ultrapassado.
 * 
 * @author Sistema
 */
public class PlanLimitExceededException extends RuntimeException {

    public PlanLimitExceededException(String message) {
        super(message);
    }

    public PlanLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}

