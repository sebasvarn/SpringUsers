--
-- -- Create the users table
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(50) NOT NULL,
                       lastname VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Insert preset data into the users table
INSERT INTO users (name, lastname, email, username, password)
VALUES
    ('Alice', 'Johnson', 'alice.johnson@example.com', 'alicej', 'password123'),
    ('Bob', 'Smith', 'bob.smith@example.com', 'bobsmith', 'password456'),
    ('Charlie', 'Brown', 'charlie.brown@example.com', 'charlieb', 'password789');

-- Create the roles table
CREATE TABLE roles (
                       role_id INT PRIMARY KEY AUTO_INCREMENT,
                       role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert preset data into the roles table
INSERT INTO roles (role_name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

-- Create the users_roles table to handle many-to-many relationship
CREATE TABLE user_roles (
                             user_id INT,
                             role_id INT,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- Insert preset data into the users_roles table to assign roles to users
INSERT INTO user_roles (user_id, role_id)
VALUES
-- Alice is both Admin and User
(1, 1), -- Alice is an Admin
(1, 2), -- Alice is also a User

-- Bob is only a User
(2, 2), -- Bob is a User

-- Charlie is only a User
(3,2);

-- -- Select query to check users and their roles
-- SELECT u.username, r.role_name
-- FROM users u
--          JOIN users_roles ur ON u.id = ur.user_id
--          JOIN roles r ON ur.role_id = r.role_id;
