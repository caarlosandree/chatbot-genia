package com.chatbotgen.aplicacao.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro HTTP para gerar trace_id único por requisição e adicionar ao MDC do Logback.
 * 
 * O trace_id é gerado como UUID v4 e incluído em todos os logs gerados durante
 * o processamento da requisição, permitindo rastrear todas as operações relacionadas.
 * 
 * @author Sistema
 */
@Component
@Order(1)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_KEY = "trace_id";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // Gera trace_id único (UUID v4) ou usa o existente no header da requisição
            String traceId = extractOrGenerateTraceId(request);
            
            // Adiciona trace_id ao MDC para incluir automaticamente em todos os logs
            MDC.put(TRACE_ID_KEY, traceId);
            
            // Adiciona trace_id no header de resposta para rastreamento frontend-backend
            response.setHeader(TRACE_ID_HEADER, traceId);
            
            // Continua o processamento da requisição
            filterChain.doFilter(request, response);
            
        } finally {
            // Limpa o MDC após processamento para evitar vazamento de contexto entre requisições
            MDC.clear();
        }
    }

    /**
     * Extrai trace_id do header da requisição ou gera um novo UUID.
     * 
     * Se a requisição já contém um trace_id no header X-Trace-Id, ele é reutilizado.
     * Caso contrário, um novo UUID v4 é gerado.
     * 
     * @param request Requisição HTTP
     * @return trace_id único
     */
    private String extractOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        
        if (traceId != null && !traceId.isBlank()) {
            return traceId;
        }
        
        return UUID.randomUUID().toString();
    }
}

