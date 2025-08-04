-- -- Exemplo de migration para adicionar uma nova coluna
-- -- Esta migration demonstra como evoluir o schema do banco

-- -- Adicionar coluna de orçamento total para a viagem
-- ALTER TABLE trips ADD total_budget DECIMAL(10,2);

-- -- Adicionar coluna de status da viagem
-- ALTER TABLE trips ADD status VARCHAR(20) DEFAULT 'PLANNING';

-- -- Adicionar constraint para valores válidos de status
-- ALTER TABLE trips ADD CONSTRAINT chk_trip_status 
--     CHECK (status IN ('PLANNING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'));

-- -- Adicionar índice para o novo status
-- CREATE INDEX idx_trips_status ON trips(status);
