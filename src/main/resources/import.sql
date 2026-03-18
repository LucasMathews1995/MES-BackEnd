INSERT INTO tb_equipamento (nome, sigla, ativo, descricao, data_ativacao, data_parado) VALUES ('Lingotamento Tiras Quente', 'LTQ', true, 'Descrição do LTQ', CURRENT_TIMESTAMP, NULL);
INSERT INTO tb_equipamento (nome, sigla, ativo, descricao, data_ativacao, data_parado) VALUES ('Linha de Decapagem', 'LDC', true, 'Descrição da LDC', CURRENT_TIMESTAMP, NULL);
INSERT INTO tb_equipamento (nome, sigla, ativo, descricao, data_ativacao, data_parado) VALUES ('Laminação a Frio', 'LAF', true, 'Descrição da LAF', CURRENT_TIMESTAMP, NULL);
INSERT INTO tb_equipamento (nome, sigla, ativo, descricao, data_ativacao, data_parado) VALUES ('Recozimento', 'RCO', false, 'Manutenção preventiva', '2026-01-01 08:00:00', CURRENT_TIMESTAMP);
INSERT INTO tb_equipamento (nome, sigla, ativo, descricao, data_ativacao, data_parado) VALUES ('Galvanização', 'GAL', true, 'Descrição da GAL', CURRENT_TIMESTAMP, NULL);

INSERT INTO tb_lote
(quantidade_disponivel, data_criacao, ordem_producao_id, programacao_id, lote_nome, descricao)
VALUES
    (15000, CURRENT_TIMESTAMP, NULL, NULL, 'A51250', 'Lote inicial de produção de chapas'), -- Comma
    (20000, CURRENT_TIMESTAMP, NULL, NULL, 'A51251', 'Segunda leva de material processado'), -- Comma
    (5000,  CURRENT_TIMESTAMP, NULL, NULL, 'A51242', 'Lote de teste de qualidade'), -- Comma
    (5000,  CURRENT_TIMESTAMP, NULL, NULL, 'A51256', 'Produção em larga escala - Setor A'), -- Comma
    (90000, CURRENT_TIMESTAMP, NULL, NULL, 'A51260', 'Lote esgotado em triagem'), -- Comma
    (12000, CURRENT_TIMESTAMP, NULL, NULL, 'A51270', 'Refugo reaproveitado'), -- Comma
    (30000, CURRENT_TIMESTAMP, NULL, NULL, 'A51299', 'Material de fornecedor externo'), -- Comma
    (85000, CURRENT_TIMESTAMP, NULL, NULL, 'A51275', 'Lote de reserva técnica'), -- Comma
    (4400,  CURRENT_TIMESTAMP, NULL, NULL, 'A51285', 'Material para construção civil'), -- Comma
    (21000, CURRENT_TIMESTAMP, NULL, NULL, 'A53299', 'Continuidade da ordem 103'), -- Comma
    (15000, CURRENT_TIMESTAMP, NULL, NULL, 'A512590', 'Protótipo de nova liga metálica'), -- Comma
    (60000, CURRENT_TIMESTAMP, NULL, NULL, 'A512951', 'Bobinas cortadas em 0.5mm'), -- Comma
    (32000, CURRENT_TIMESTAMP, NULL, NULL, 'A526669', 'Bobinas cortadas em 1.0mm'), -- Comma
    (9000,  CURRENT_TIMESTAMP, NULL, NULL, 'A52250', 'Ajuste de estoque'), -- Comma
    (11000, CURRENT_TIMESTAMP, NULL, NULL, 'A54250', 'Segunda remessa fornecedor'), -- Comma
    (2500,  CURRENT_TIMESTAMP, NULL, NULL, 'A57250', 'Pedido especial sob medida'), -- Comma
    (18000, CURRENT_TIMESTAMP, NULL, NULL, 'A91250', 'Lote de vigas estruturais'), -- Comma
    (7500,  CURRENT_TIMESTAMP, NULL, NULL, 'A81250', 'Finalização de turno A'), -- Comma
    (40000, CURRENT_TIMESTAMP, NULL, NULL, 'A51265', 'Lote destinado à exportação'), -- Comma
    (50000, CURRENT_TIMESTAMP, NULL, NULL, 'A53278', 'Material para manutenção interna'); -- FINAL Semicolon

