package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando um token JWT é inválido ou expirado.
 * 
 * @author Sistema
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

