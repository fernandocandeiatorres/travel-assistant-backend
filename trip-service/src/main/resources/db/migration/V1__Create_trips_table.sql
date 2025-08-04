-- Migration inicial para o trip-service
-- Cria a tabela trips

CREATE TABLE trips (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL,
    destination VARCHAR(255) NOT NULL,
    starts_at DATE NOT NULL,
    ends_at DATE NOT NULL,
    is_confirmed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Índices para melhorar performance
CREATE INDEX idx_trips_user_id ON trips(user_id);
CREATE INDEX idx_trips_destination ON trips(destination);
CREATE INDEX idx_trips_dates ON trips(starts_at, ends_at);
