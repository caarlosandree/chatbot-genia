package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando o usuário não tem permissão para realizar uma operação.
 * 
 * @author Sistema
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}

