package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.dto.auth.JwtResponse;
import com.chatbotgen.aplicacao.dto.auth.LoginRequest;
import com.chatbotgen.aplicacao.dto.auth.RefreshTokenRequest;
import com.chatbotgen.aplicacao.exception.InvalidTokenException;
import com.chatbotgen.aplicacao.exception.UnauthorizedException;
import com.chatbotgen.aplicacao.exception.UserNotFoundException;
import com.chatbotgen.aplicacao.model.User;
import com.chatbotgen.aplicacao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Serviço responsável pela autenticação e gerenciamento de tokens.
 * 
 * @author Sistema
 */
@Service
public class AuthService {

    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            StringRedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Realiza o login do usuário, gerando tokens de acesso e renovação.
     * 
     * @param request Dados de login (email e senha)
     * @return Tokens JWT (access e refresh)
     * @throws UnauthorizedException se as credenciais forem inválidas
     */
    @Transactional
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

        if (!user.getActive()) {
            throw new UnauthorizedException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        // Gera tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Salva refresh token no Redis com expiração
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + refreshToken;
        redisTemplate.opsForValue().set(
                refreshTokenKey,
                user.getId().toString(),
                Duration.ofMillis(refreshTokenExpiration)
        );

        // Atualiza último login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return new JwtResponse(
                accessToken,
                refreshToken,
                refreshTokenExpiration
        );
    }

    /**
     * Renova os tokens usando um refresh token válido.
     * Implementa rotação de tokens (apaga o antigo e gera novos).
     * 
     * @param request Refresh token
     * @return Novos tokens JWT
     * @throws InvalidTokenException se o refresh token for inválido ou expirado
     */
    @Transactional
    public JwtResponse refresh(RefreshTokenRequest request) {
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + request.refreshToken();
        String userIdStr = redisTemplate.opsForValue().get(refreshTokenKey);

        if (userIdStr == null) {
            throw new InvalidTokenException("Refresh token inválido ou expirado");
        }

        Long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!user.getActive()) {
            throw new UnauthorizedException("Usuário inativo");
        }

        // Remove o refresh token antigo (rotação de tokens)
        redisTemplate.delete(refreshTokenKey);

        // Gera novos tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Salva novo refresh token no Redis
        String newRefreshTokenKey = REFRESH_TOKEN_PREFIX + newRefreshToken;
        redisTemplate.opsForValue().set(
                newRefreshTokenKey,
                user.getId().toString(),
                Duration.ofMillis(refreshTokenExpiration)
        );

        return new JwtResponse(
                newAccessToken,
                newRefreshToken,
                refreshTokenExpiration
        );
    }

    /**
     * Realiza logout, removendo o refresh token do Redis.
     * 
     * @param refreshToken Refresh token a ser invalidado
     */
    public void logout(String refreshToken) {
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + refreshToken;
        redisTemplate.delete(refreshTokenKey);
    }
}

