DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS priorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS filters;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

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
    priority_id INT REFERENCES priorities(id) NOT NULL,
    user_id INT REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS items_categories (
    item_id INT REFERENCES items(id) NOT NULL,
    categories_id INT REFERENCES categories(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS filters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

INSERT INTO priorities(value, name)
VALUES (3, 'Low priority'), (2, 'Middle priority'), (1, 'High priority');

INSERT INTO filters(name)
VALUES ('All'), ('Completed'), ('Not completed'), ('High priority');

INSERT INTO categories(name)
VALUES ('Personal'), ('Work'), ('Education');