CREATE TABLE author(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    pw VARCHAR
);

CREATE TABLE card(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    status VARCHAR,
    content TEXT,
    category VARCHAR
);
