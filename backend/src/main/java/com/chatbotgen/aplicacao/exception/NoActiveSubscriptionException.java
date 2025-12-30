package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando uma organização não possui assinatura ativa.
 * 
 * @author Sistema
 */
public class NoActiveSubscriptionException extends RuntimeException {

    public NoActiveSubscriptionException(String message) {
        super(message);
    }

    public NoActiveSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

