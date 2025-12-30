-- Migration: V3__Create_plans_table.sql
-- Descrição: Cria a tabela de planos e insere planos predefinidos
-- Autor: Sistema
-- Data: 2025-12-30

-- Cria a tabela de planos
CREATE TABLE plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    max_users INTEGER NOT NULL,
    max_chatbots INTEGER NOT NULL,
    max_phone_numbers INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Cria índices
CREATE INDEX idx_plans_name ON plans(name);
CREATE INDEX idx_plans_active ON plans(active);

-- Comentários nas colunas
COMMENT ON TABLE plans IS 'Tabela de planos disponíveis no sistema';
COMMENT ON COLUMN plans.id IS 'Identificador único do plano';
COMMENT ON COLUMN plans.name IS 'Nome do plano';
COMMENT ON COLUMN plans.description IS 'Descrição do plano';
COMMENT ON COLUMN plans.price IS 'Preço mensal do plano em reais';
COMMENT ON COLUMN plans.max_users IS 'Limite máximo de usuários (-1 para ilimitado)';
COMMENT ON COLUMN plans.max_chatbots IS 'Limite máximo de chatbots (-1 para ilimitado)';
COMMENT ON COLUMN plans.max_phone_numbers IS 'Limite máximo de números de telefone (-1 para ilimitado)';
COMMENT ON COLUMN plans.active IS 'Indica se o plano está ativo';
COMMENT ON COLUMN plans.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN plans.updated_at IS 'Data e hora da última atualização do registro';

-- Insere planos predefinidos
INSERT INTO plans (name, description, price, max_users, max_chatbots, max_phone_numbers, active) VALUES
('Básico', 'Plano ideal para pequenas empresas começando com chatbots', 99.00, 5, 3, 1, true),
('Premium', 'Plano completo para empresas em crescimento', 299.00, 20, 10, 5, true),
('Enterprise', 'Plano ilimitado para grandes empresas', 999.00, -1, -1, -1, true);

