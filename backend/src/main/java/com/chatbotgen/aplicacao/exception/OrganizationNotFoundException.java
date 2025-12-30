package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando uma organização não é encontrada.
 * 
 * @author Sistema
 */
public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public OrganizationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

