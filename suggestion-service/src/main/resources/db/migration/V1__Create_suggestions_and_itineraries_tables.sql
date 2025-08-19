CREATE TABLE suggestions (
    id UUID PRIMARY KEY,
    trip_id UUID,
    destination VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE itineraries (
    id UUID PRIMARY KEY,
    budget_type VARCHAR(255) NOT NULL,
    logging_details TEXT,
    restaurant_details TEXT,
    activity_details TEXT,
    suggestion_id UUID NOT NULL,
    FOREIGN KEY (suggestion_id) REFERENCES suggestions(id)
);
