CREATE TABLE locacaoitem (
    id BIGSERIAL PRIMARY KEY,
    locacao_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    valor DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_locacaoitem_locacao FOREIGN KEY (locacao_id) REFERENCES locacao(id),
    CONSTRAINT fk_locacaoitem_item FOREIGN KEY (item_id) REFERENCES item(id),
    CONSTRAINT uk_locacaoitem_locacao_item UNIQUE (locacao_id, item_id)
);

CREATE INDEX idx_locacaoitem_locacao_id ON locacaoitem(locacao_id);
CREATE INDEX idx_locacaoitem_item_id ON locacaoitem(item_id);
