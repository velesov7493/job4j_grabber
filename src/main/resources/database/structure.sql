DROP TABLE IF EXISTS tz_rabbits;
DROP TABLE IF EXISTS tz_posts;

CREATE TABLE tz_rabbits (
    id SERIAL PRIMARY KEY,
    created_date TIMESTAMP
);

CREATE TABLE tz_posts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    link VARCHAR(255) NOT NULL UNIQUE,
    authorName VARCHAR(90),
    text TEXT,
    created TIMESTAMP NOT NULL
);