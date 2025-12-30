package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando uma assinatura não é encontrada.
 * 
 * @author Sistema
 */
public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }

    public SubscriptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

