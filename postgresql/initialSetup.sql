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

INSERT INTO author VALUES(DEFAULT, 'test', '炴㲮鯸쵇厳夗ₙ㒞즞혅듞儠鮈馽ᯑ땶슚鬧㔕Ḥ');
