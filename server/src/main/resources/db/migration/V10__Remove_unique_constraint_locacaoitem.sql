-- Remove a constraint única que impede o mesmo item de ser adicionado múltiplas vezes
ALTER TABLE locacaoitem DROP CONSTRAINT IF EXISTS uk_locacaoitem_locacao_item;
