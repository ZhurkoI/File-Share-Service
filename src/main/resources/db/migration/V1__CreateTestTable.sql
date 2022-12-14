CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filepath TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    type VARCHAR(255) NOT NULL,
    file_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_file
        FOREIGN KEY (file_id)
            REFERENCES file (id)
            ON DELETE CASCADE,
    CONSTRAINT FK_user
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE CASCADE
);