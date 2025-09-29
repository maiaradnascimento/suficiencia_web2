INSERT INTO cliente (nome, cpf) VALUES 
('João', '12345678901'),
('Maria', '98765432100');

INSERT INTO item (nome, valor) VALUES 
('Chuteira', 45.00),
('Luva', 25.50);

INSERT INTO locacao (numnota, data, cliente_id) VALUES 
('0001', '2024-09-15', (SELECT id FROM cliente WHERE cpf = '12345678901')),
('0002', '2024-09-20', (SELECT id FROM cliente WHERE cpf = '98765432100'));

INSERT INTO locacaoitem (locacao_id, item_id, quantidade, valor) VALUES 
((SELECT id FROM locacao WHERE numnota = '0001'), (SELECT id FROM item WHERE nome = 'Chuteira'), 1, 45.00),
((SELECT id FROM locacao WHERE numnota = '0002'), (SELECT id FROM item WHERE nome = 'Luva'), 2, 25.50);
