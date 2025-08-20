-- Script para inicializar múltiplos bancos de dados
-- Este script é executado automaticamente quando o container PostgreSQL é criado pela primeira vez

-- Criar banco para o trip-service (se não existir)
CREATE DATABASE trip_service_db;

-- Criar banco para o suggestion-service  
CREATE DATABASE suggestion_service_db;

-- Criação do banco de dados para o Auth Service
CREATE DATABASE auth_service_db;

-- Conectar ao banco de dados auth_service_db e criar a tabela de usuários
\c auth_service_db;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Opcional: criar usuários específicos para cada serviço (mais seguro)
-- CREATE USER trip_user WITH PASSWORD 'trip_password';
-- CREATE USER suggestion_user WITH PASSWORD 'suggestion_password';

-- GRANT ALL PRIVILEGES ON DATABASE trip_service_db TO trip_user;
-- GRANT ALL PRIVILEGES ON DATABASE suggestion_service_db TO suggestion_user;
