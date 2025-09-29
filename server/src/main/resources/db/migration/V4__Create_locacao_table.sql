CREATE TABLE locacao (
    id BIGSERIAL PRIMARY KEY,
    numnota VARCHAR(50) NOT NULL UNIQUE,
    data DATE NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_locacao_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE INDEX idx_locacao_numnota ON locacao(numnota);
CREATE INDEX idx_locacao_data ON locacao(data);
CREATE INDEX idx_locacao_cliente_id ON locacao(cliente_id);
