package com.chatbotgen.aplicacao.exception;

/**
 * Exceção lançada quando há falha de autenticação (credenciais inválidas).
 * 
 * @author Sistema
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

