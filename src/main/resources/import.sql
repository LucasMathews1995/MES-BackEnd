
INSERT INTO tb_equipamento
(ativo, data_ativacao, data_parado,  nome, descricao, sigla, status_equipamento)
VALUES
    (true, CURRENT_TIMESTAMP, null,  'Alto Forno 01', 'Produção de gusa líquido', 'AF01', 'OPERANDO'),
    (true, CURRENT_TIMESTAMP, null,  'Laminador de Tiras a Quente', 'Redução de espessura de placas', 'LTQ-02', 'OPERANDO'),
    (false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  'Conversor de Aço 02', 'Transformação de gusa em aço', 'CONV-02', 'PARADO'),
    (true, CURRENT_TIMESTAMP, null,  'Ponte Rolante de Lingotamento', 'Movimentação de panelas de aço', 'PR-04', 'OPERANDO'),
    (true, CURRENT_TIMESTAMP, null,  'Forno de Reaquecimento', 'Aquecimento de placas para laminação', 'FR-05', 'OPERANDO');

INSERT INTO tb_ordem_producao
(data_criacao, data_encerramento,  ordem_venda_id, numero_op, status)
VALUES
    (CURRENT_TIMESTAMP, null,  null, 'OP2024001', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024002', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024003', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024004', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024005', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024006', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024007', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024008', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024009', 'INICIADA'),
    (CURRENT_TIMESTAMP, null,  null, 'OP2024010', 'INICIADA');


INSERT INTO tb_lote
(quantidade_disponivel, sequencia, data_criacao,  ordem_producao_id, nome, descricao, status)
VALUES
        (20000, 2, CURRENT_TIMESTAMP, 2,  'A583580101', 'Descrição do item B', 'ABASTECIDO'),
    (30000, 3, CURRENT_TIMESTAMP, 3,  'A583580102', 'Descrição do item C', 'ABASTECIDO'),
    (40000, 4, CURRENT_TIMESTAMP, 3,  'A583580103', 'Descrição do item D', 'ABASTECIDO'),
    (50000, 5, CURRENT_TIMESTAMP, 1,  'A583580104', 'Descrição do item E', 'ABASTECIDO'),
    (60000, 6, CURRENT_TIMESTAMP, 2,  'A583580105', 'Descrição do item F', 'ABASTECIDO'),
    (70000, 7, CURRENT_TIMESTAMP, 1,  'A583580106', 'Descrição do item G', 'ABASTECIDO'),
    (80000, 8, CURRENT_TIMESTAMP, 3,  'A583580107', 'Descrição do item H', 'ABASTECIDO'),
    (90000, 9, CURRENT_TIMESTAMP, 4,  'A583580108', 'Descrição do item I', 'ABASTECIDO'),
    (100000, 10, CURRENT_TIMESTAMP, 5,  'A583580109', 'Descrição do item J', 'ABASTECIDO'),
    (110000, 11, CURRENT_TIMESTAMP, 3,  'A583580110', 'Descrição do item K', 'ABASTECIDO'),
    (120000, 12, CURRENT_TIMESTAMP, 2,  'A583580111', 'Descrição do item L', 'ABASTECIDO'),
    (130000, 13, CURRENT_TIMESTAMP, 2,  'A583580112', 'Descrição do item M', 'ABASTECIDO'),
    (10000, 1, CURRENT_TIMESTAMP, 1,  'A583580100', 'Descrição do item A', 'ABASTECIDO'),
    (140000, 14, CURRENT_TIMESTAMP, 4,  'A583580113', 'Descrição do item N', 'ABASTECIDO'),
    (150000, 15, CURRENT_TIMESTAMP, 5,  'A583580114', 'Descrição do item O', 'ABASTECIDO'),
    (160000, 16, CURRENT_TIMESTAMP, 6,  'A583580115', 'Descrição do item P', 'ABASTECIDO'),
    (170000, 17, CURRENT_TIMESTAMP, 7,  'A583580116', 'Descrição do item Q', 'ABASTECIDO'),
    (180000, 18, CURRENT_TIMESTAMP, 7,  'A583580117', 'Descrição do item R', 'ABASTECIDO'),
    (190000, 19, CURRENT_TIMESTAMP, 5,  'A583580118', 'Descrição do item S', 'ABASTECIDO'),
    (200000, 20, CURRENT_TIMESTAMP, 4,  'A583580119', 'Descrição do item T', 'ABASTECIDO'),
    (210000, 21, CURRENT_TIMESTAMP, 3,  'A583580120', 'Descrição do item U', 'ABASTECIDO'),
    (220000, 22, CURRENT_TIMESTAMP, 2,  'A583580121', 'Descrição do item V', 'ABASTECIDO'),
    (230000, 23, CURRENT_TIMESTAMP, 5,  'A583580122', 'Descrição do item W', 'ABASTECIDO'),
    (240000, 24, CURRENT_TIMESTAMP, 4,  'A583580123', 'Descrição do item X', 'ABASTECIDO'),
    (250000, 25, CURRENT_TIMESTAMP, 6,  'A583580124', 'Descrição do item Y', 'ABASTECIDO');


