CREATE TABLE author(
    name VARCHAR PRIMARY KEY,
    password VARCHAR
);

CREATE TABLE card(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    status VARCHAR,
    content TEXT,
    category VARCHAR
);

INSERT INTO author VALUES(DEFAULT, 'test', '炴㲮鯸쵇厳夗ₙ㒞즞혅듞儠鮈馽ᯑ땶슚鬧㔕Ḥ');
