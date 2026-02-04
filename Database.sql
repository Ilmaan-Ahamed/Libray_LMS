CREATE DATABASE Library_LMS_DB;
GO

USE Library_LMS_DB;
GO

CREATE TABLE books (
    book_id INT IDENTITY(1,1) PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available BIT DEFAULT 1
);

CREATE TABLE users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE issued_books (
    issue_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    book_id INT,
    issue_date DATE,
    return_status BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

INSERT INTO books (title, author, available) VALUES
('Clean Code', 'Robert C. Martin', 1),
('Effective Java', 'Joshua Bloch', 1),
('Design Patterns', 'Erich Gamma', 1),
('Introduction to Algorithms', 'Thomas H. Cormen', 1),
('Head First Java', 'Kathy Sierra', 1);

SELECT * FROM books;