CREATE TABLE category (
id INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(100) NOT NULL UNIQUE,
descricao TEXT
);

INSERT INTO category (nome, descricao) VALUES
('Hatch', 'Veículos pequenos e compactos, ideais para cidade.'),
('Sedan', 'Veículos médios com porta-malas maior e conforto para família.'),
('SUV', 'Veículos maiores, com maior altura e espaço interno.'),
('Pick-up', 'Veículos utilitários para transporte de carga.'),
('Esportivo', 'Carros com desempenho elevado e design esportivo.'),
('Elétrico', 'Veículos movidos a energia elétrica.'),
('Conversível', 'Veículos com capota retrátil ou removível.'),
('Minivan', 'Veículos familiares com maior capacidade de passageiros.'),
('Crossover', 'Mistura de SUV com carros de passeio.'),
('Off-road', 'Veículos preparados para terrenos difíceis e trilhas.');