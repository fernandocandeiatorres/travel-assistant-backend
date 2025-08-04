-- Script para inicializar múltiplos bancos de dados
-- Este script é executado automaticamente quando o container PostgreSQL é criado pela primeira vez

-- Criar banco para o trip-service (se não existir)
CREATE DATABASE trip_service_db;

-- Criar banco para o suggestion-service  
CREATE DATABASE suggestion_service_db;

-- Opcional: criar usuários específicos para cada serviço (mais seguro)
-- CREATE USER trip_user WITH PASSWORD 'trip_password';
-- CREATE USER suggestion_user WITH PASSWORD 'suggestion_password';
-- 
-- GRANT ALL PRIVILEGES ON DATABASE trip_service_db TO trip_user;
-- GRANT ALL PRIVILEGES ON DATABASE suggestion_service_db TO suggestion_user;
