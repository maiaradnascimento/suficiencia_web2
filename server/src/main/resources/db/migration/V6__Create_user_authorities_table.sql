-- Create user_authorities table for storing user roles
CREATE TABLE user_authorities (
    user_id BIGINT NOT NULL,
    authority VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, authority),
    CONSTRAINT fk_user_authorities_user FOREIGN KEY (user_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX idx_user_authorities_user_id ON user_authorities(user_id);
