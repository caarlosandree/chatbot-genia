-- Migration: V5__Create_subscriptions_table.sql
-- Descrição: Cria a tabela de assinaturas
-- Autor: Sistema
-- Data: 2025-12-30

-- Cria a tabela de assinaturas
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subscriptions_organization 
        FOREIGN KEY (organization_id) 
        REFERENCES organizations(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_plan 
        FOREIGN KEY (plan_id) 
        REFERENCES plans(id) 
        ON DELETE RESTRICT
);

-- Cria índices
CREATE INDEX idx_subscriptions_organization_id ON subscriptions(organization_id);
CREATE INDEX idx_subscriptions_plan_id ON subscriptions(plan_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);

-- Comentários nas colunas
COMMENT ON TABLE subscriptions IS 'Tabela de assinaturas de planos por organizações';
COMMENT ON COLUMN subscriptions.id IS 'Identificador único da assinatura';
COMMENT ON COLUMN subscriptions.organization_id IS 'Identificador da organização';
COMMENT ON COLUMN subscriptions.plan_id IS 'Identificador do plano';
COMMENT ON COLUMN subscriptions.status IS 'Status da assinatura (ACTIVE, INACTIVE, EXPIRED, CANCELLED)';
COMMENT ON COLUMN subscriptions.start_date IS 'Data de início da assinatura';
COMMENT ON COLUMN subscriptions.end_date IS 'Data de término da assinatura (NULL para assinaturas sem término)';
COMMENT ON COLUMN subscriptions.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN subscriptions.updated_at IS 'Data e hora da última atualização do registro';

