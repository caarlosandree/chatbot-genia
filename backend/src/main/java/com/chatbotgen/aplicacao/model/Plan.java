package com.chatbotgen.aplicacao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um plano disponível no sistema.
 * 
 * @author Sistema
 */
@Entity
@Table(name = "plans", indexes = {
    @Index(name = "idx_plans_name", columnList = "name"),
    @Index(name = "idx_plans_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome do plano é obrigatório")
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser positivo ou zero")
    private BigDecimal price;

    @Column(nullable = false, name = "max_users")
    @NotNull(message = "Limite de usuários é obrigatório")
    private Integer maxUsers;

    @Column(nullable = false, name = "max_chatbots")
    @NotNull(message = "Limite de chatbots é obrigatório")
    private Integer maxChatbots;

    @Column(nullable = false, name = "max_phone_numbers")
    @NotNull(message = "Limite de números de telefone é obrigatório")
    private Integer maxPhoneNumbers;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Verifica se o limite de usuários é ilimitado (-1 ou null).
     * 
     * @return true se for ilimitado
     */
    public boolean isUnlimitedUsers() {
        return maxUsers != null && maxUsers == -1;
    }

    /**
     * Verifica se o limite de chatbots é ilimitado (-1 ou null).
     * 
     * @return true se for ilimitado
     */
    public boolean isUnlimitedChatbots() {
        return maxChatbots != null && maxChatbots == -1;
    }

    /**
     * Verifica se o limite de números de telefone é ilimitado (-1 ou null).
     * 
     * @return true se for ilimitado
     */
    public boolean isUnlimitedPhoneNumbers() {
        return maxPhoneNumbers != null && maxPhoneNumbers == -1;
    }
}

