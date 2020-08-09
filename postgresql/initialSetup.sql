CREATE TABLE author(
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE,
    password VARCHAR
);

CREATE TABLE card(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    status VARCHAR(20),
    content TEXT,
    category VARCHAR(20),
    author INTEGER REFERENCES author(id)
);
