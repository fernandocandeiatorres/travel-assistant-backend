-- Migration inicial para o suggestion-service
-- Cria as tabelas suggestions e itineraries

-- Tabela suggestions
CREATE TABLE suggestions (
    id UUID NOT NULL PRIMARY KEY,
    trip_id UUID NOT NULL,
    destination VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela itineraries
CREATE TABLE itineraries (
    id UUID NOT NULL PRIMARY KEY,
    budget_type VARCHAR(20) NOT NULL CHECK (budget_type IN ('ECONOMIC', 'MEDIUM', 'LUXURY')),
    logging_details TEXT,
    restaurant_details TEXT,
    activity_details TEXT,
    suggestion_id UUID NOT NULL,
    CONSTRAINT fk_itineraries_suggestion 
        FOREIGN KEY (suggestion_id) REFERENCES suggestions(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX idx_suggestions_trip_id ON suggestions(trip_id);
CREATE INDEX idx_suggestions_destination ON suggestions(destination);
CREATE INDEX idx_itineraries_suggestion_id ON itineraries(suggestion_id);
CREATE INDEX idx_itineraries_budget_type ON itineraries(budget_type);
