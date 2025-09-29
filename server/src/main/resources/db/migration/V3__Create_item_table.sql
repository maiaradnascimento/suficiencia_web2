CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

CREATE INDEX idx_item_nome ON item(nome);
