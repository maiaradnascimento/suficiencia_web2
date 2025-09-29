-- Senha: Admin123
INSERT INTO usuario (email, nome, senha) 
VALUES ('admin@locadora.com', 'Administrador', '$2a$10$9WRaNE8GDS3cfC727UVw/O8FHSGDWin7EBgcZkD9WzyeMkS.Kbvbi');

INSERT INTO user_authorities (user_id, authority)
SELECT id, 'ROLE_ADMIN' FROM usuario WHERE email = 'admin@locadora.com';

INSERT INTO user_authorities (user_id, authority)
SELECT id, 'ROLE_USER' FROM usuario WHERE email = 'admin@locadora.com';
