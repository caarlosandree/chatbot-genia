-- Migration: V1__Initial_baseline.sql
-- Descrição: Migration inicial para configurar o baseline do Flyway
-- Autor: Sistema
-- Data: 2025-12-30

-- Esta migration marca o baseline inicial do banco de dados.
-- O Flyway criará automaticamente a tabela flyway_schema_history
-- para rastrear o versionamento das migrations.

-- Comentários e documentação do schema inicial
COMMENT ON SCHEMA public IS 'Schema público do banco de dados chatbot-dev';

-- Esta migration está vazia intencionalmente para marcar o baseline.
-- As próximas migrations conterão as definições de tabelas, índices e constraints.

