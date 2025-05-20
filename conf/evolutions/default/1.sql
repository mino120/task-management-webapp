-- !Ups

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE lists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    board_id BIGINT NOT NULL,
    position INT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES boards(id)
);

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    list_id BIGINT NOT NULL,
    position INT NOT NULL,
    due_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (list_id) REFERENCES lists(id)
);

-- Insert a demo user
INSERT INTO users (name, email, password) VALUES ('Demo User', 'demo@example.com', 'password');

-- Insert a demo board
INSERT INTO boards (name, description, user_id) VALUES ('My First Board', 'This is a demo board', 1);

-- Insert demo lists
INSERT INTO lists (name, board_id, position) VALUES ('To Do', 1, 0);
INSERT INTO lists (name, board_id, position) VALUES ('In Progress', 1, 1);
INSERT INTO lists (name, board_id, position) VALUES ('Done', 1, 2);

-- Insert demo tasks
INSERT INTO tasks (title, description, list_id, position) VALUES ('Learn Scala', 'Study Scala programming language basics', 1, 0);
INSERT INTO tasks (title, description, list_id, position) VALUES ('Create Task App', 'Build a simple task management application', 1, 1);
INSERT INTO tasks (title, description, list_id, position) VALUES ('Setup Project', 'Initialize project structure', 2, 0);
INSERT INTO tasks (title, description, list_id, position) VALUES ('Complete Assignment', 'Submit the final project', 3, 0);

-- !Downs

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS lists;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS users;