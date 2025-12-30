-- Migration: V4__Create_organizations_table.sql
-- Descrição: Cria a tabela de organizações e adiciona coluna organization_id em users
-- Autor: Sistema
-- Data: 2025-12-30

-- Cria a tabela de organizações
CREATE TABLE organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Cria índices
CREATE INDEX idx_organizations_document ON organizations(document);
CREATE INDEX idx_organizations_email ON organizations(email);

-- Comentários nas colunas
COMMENT ON TABLE organizations IS 'Tabela de organizações/empresas do sistema';
COMMENT ON COLUMN organizations.id IS 'Identificador único da organização';
COMMENT ON COLUMN organizations.name IS 'Nome da organização';
COMMENT ON COLUMN organizations.document IS 'CNPJ ou CPF da organização (único)';
COMMENT ON COLUMN organizations.email IS 'Email de contato da organização';
COMMENT ON COLUMN organizations.phone IS 'Telefone de contato da organização';
COMMENT ON COLUMN organizations.active IS 'Indica se a organização está ativa';
COMMENT ON COLUMN organizations.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN organizations.updated_at IS 'Data e hora da última atualização do registro';

-- Adiciona coluna organization_id na tabela users
ALTER TABLE users ADD COLUMN organization_id BIGINT;

-- Cria índice para a nova coluna
CREATE INDEX idx_users_organization_id ON users(organization_id);

-- Adiciona foreign key constraint
ALTER TABLE users 
ADD CONSTRAINT fk_users_organization 
FOREIGN KEY (organization_id) 
REFERENCES organizations(id) 
ON DELETE SET NULL;

-- Comentário na nova coluna
COMMENT ON COLUMN users.organization_id IS 'Identificador da organização à qual o usuário pertence';

