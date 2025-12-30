package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando um usuário não é encontrado.
 * 
 * @author Sistema
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

