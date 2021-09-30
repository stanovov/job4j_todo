DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS priorities;
DROP TABLE IF EXISTS filters;

CREATE TABLE IF NOT EXISTS priorities (
    id SERIAL PRIMARY KEY,
    value INT NOT NULL CHECK (value > 0),
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    done BOOLEAN NOT NULL,
    priority_id INT REFERENCES priorities(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS filters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

INSERT INTO priorities(value, name)
VALUES (3, 'Low priority'), (2, 'Middle priority'), (1, 'High priority');

INSERT INTO filters(name)
VALUES ('All'), ('Completed'), ('Not completed'), ('High priority');