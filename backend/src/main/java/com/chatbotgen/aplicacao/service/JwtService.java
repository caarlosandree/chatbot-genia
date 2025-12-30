package com.chatbotgen.aplicacao.service;

import com.chatbotgen.aplicacao.model.Role;
import com.chatbotgen.aplicacao.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Serviço responsável por gerar e validar tokens JWT.
 * 
 * @author Sistema
 */
@Service
public class JwtService {

    @Value("${app.jwt.secret-key}")
    private String secretKey;

    @Value("${app.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    /**
     * Gera um Access Token JWT para o usuário.
     * 
     * @param user Usuário para o qual o token será gerado
     * @return Access Token JWT
     */
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Gera um Refresh Token (UUID v4).
     * 
     * @param user Usuário para o qual o token será gerado
     * @return Refresh Token (UUID)
     */
    public String generateRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }

    /**
     * Extrai todas as claims do token JWT.
     * 
     * @param token Token JWT
     * @return Claims do token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extrai uma claim específica do token JWT.
     * 
     * @param token Token JWT
     * @param claimsResolver Função para extrair a claim específica
     * @return Valor da claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai o email (subject) do token JWT.
     * 
     * @param token Token JWT
     * @return Email do usuário
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai o role do token JWT.
     * 
     * @param token Token JWT
     * @return Role do usuário
     */
    public Role extractRole(String token) {
        return Role.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
    }

    /**
     * Extrai o ID do usuário do token JWT.
     * 
     * @param token Token JWT
     * @return ID do usuário
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Valida se o token JWT é válido (assinatura e expiração).
     * 
     * @param token Token JWT
     * @return true se válido, false caso contrário
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retorna as authorities do Spring Security baseadas no role do token.
     * 
     * @param token Token JWT
     * @return Lista de authorities
     */
    public List<GrantedAuthority> extractAuthorities(String token) {
        Role role = extractRole(token);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Obtém a chave de assinatura a partir da secret key.
     * 
     * @return Chave secreta para assinatura
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

