CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS persons (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS occasions (
    id UUID PRIMARY KEY,
    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    type VARCHAR(30) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_ideas (
    id UUID PRIMARY KEY,
    person_id UUID NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
    occasion_id UUID REFERENCES occasions(id) ON DELETE SET NULL,
    title VARCHAR(120) NOT NULL,
    estimated_price NUMERIC(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    notes VARCHAR(500) NOT NULL
);
