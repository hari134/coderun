CREATE TABLE languages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Insert seed data into the languages table
INSERT INTO languages (name) VALUES ('python');
INSERT INTO languages (name) VALUES ('cpp');
INSERT INTO languages (name) VALUES ('java');
