CREATE TABLE vehicle (
    placa VARCHAR(10) PRIMARY KEY,
    id_categoria INT NOT NULL,
    id_seguro INT NOT NULL,
    id_brand INT NOT NULL,
    id_modelo INT NOT NULL,
    ano INT,
    id_cor INT NOT NULL,
    status VARCHAR(20) DEFAULT 'DISPONIVEL',
    descricao TEXT,
    FOREIGN KEY (id_categoria) REFERENCES category(id),
    FOREIGN KEY (id_seguro) REFERENCES insurance(id),
    FOREIGN KEY (id_brand) REFERENCES brand(id),
    FOREIGN KEY (id_modelo) REFERENCES model(id),
    FOREIGN KEY (id_cor) REFERENCES color(id)
);

INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('CUR6F56', 6, 2, 8, 22, 2019, 8, 'ALUGADO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('HIS0E11', 6, 5, 7, 21, 2018, 9, 'DISPONIVEL', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('QNR7S37', 5, 1, 7, 21, 2015, 9, 'MANUTENCAO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ZSV9E17', 3, 2, 4, 10, 2017, 1, 'ALUGADO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('VNG2T45', 2, 1, 7, 21, 2021, 9, 'ALUGADO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('QOA9S86', 2, 4, 3, 7, 2024, 10, 'MANUTENCAO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('DSY1Z48', 6, 4, 5, 14, 2025, 8, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('PBF4M29', 10, 4, 1, 2, 2020, 4, 'DISPONIVEL', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ILA5Z44', 5, 4, 2, 4, 2017, 5, 'MANUTENCAO', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YKI6T13', 8, 2, 7, 21, 2022, 9, 'ALUGADO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('QZF1J86', 3, 5, 10, 28, 2017, 4, 'DISPONIVEL', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('HBO0L34', 6, 4, 9, 26, 2025, 2, 'MANUTENCAO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('HLP9O27', 10, 5, 6, 17, 2017, 5, 'MANUTENCAO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('JAM0F93', 4, 2, 4, 11, 2025, 7, 'MANUTENCAO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YYV0P22', 10, 1, 4, 11, 2024, 5, 'MANUTENCAO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NML7I37', 5, 5, 10, 29, 2024, 2, 'DISPONIVEL', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('VLR9T44', 2, 4, 2, 5, 2019, 6, 'ALUGADO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UWU4G28', 4, 1, 10, 29, 2025, 6, 'MANUTENCAO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('XAU7J46', 7, 4, 1, 3, 2024, 4, 'ALUGADO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('VRU7V58', 5, 2, 9, 25, 2019, 2, 'ALUGADO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('XIX8Y25', 2, 2, 6, 16, 2019, 7, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ISJ7D70', 10, 5, 10, 28, 2022, 4, 'MANUTENCAO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('VMQ4N02', 3, 2, 6, 17, 2024, 8, 'DISPONIVEL', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('WFY1P52', 6, 1, 1, 2, 2019, 4, 'DISPONIVEL', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YFP1E96', 7, 4, 7, 20, 2021, 1, 'DISPONIVEL', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('GXL0K83', 1, 1, 4, 10, 2020, 5, 'DISPONIVEL', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('MQS4F10', 5, 3, 8, 24, 2023, 10, 'MANUTENCAO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NVE5P53', 3, 4, 1, 1, 2018, 3, 'DISPONIVEL', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('SQF1L06', 5, 5, 10, 28, 2024, 1, 'MANUTENCAO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UAB1N76', 2, 5, 5, 14, 2017, 4, 'MANUTENCAO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('JNV1V84', 10, 5, 3, 8, 2016, 9, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('DJD1P39', 4, 3, 9, 25, 2020, 7, 'ALUGADO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BRP3W75', 4, 1, 1, 3, 2016, 2, 'MANUTENCAO', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ECY8C14', 4, 4, 5, 14, 2022, 1, 'DISPONIVEL', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BJL5N11', 1, 1, 3, 9, 2025, 3, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('OTI1L52', 2, 4, 7, 20, 2019, 7, 'MANUTENCAO', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ZNU2D20', 3, 1, 7, 21, 2022, 10, 'MANUTENCAO', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('FTM5T02', 8, 4, 2, 5, 2022, 1, 'DISPONIVEL', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UKA8Y99', 4, 3, 9, 27, 2024, 2, 'ALUGADO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('PLV6E89', 1, 1, 3, 9, 2022, 8, 'DISPONIVEL', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YPK3K40', 9, 2, 9, 27, 2021, 7, 'DISPONIVEL', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('OOZ9O17', 8, 3, 2, 5, 2025, 1, 'DISPONIVEL', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NBR8A19', 10, 3, 9, 27, 2018, 8, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BHO5R99', 8, 3, 8, 22, 2016, 4, 'DISPONIVEL', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('LUZ4R85', 2, 1, 3, 8, 2017, 7, 'DISPONIVEL', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('TGG1M37', 4, 3, 2, 5, 2015, 10, 'DISPONIVEL', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('OVS8E70', 9, 1, 9, 26, 2024, 8, 'MANUTENCAO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('WSF2D69', 10, 3, 9, 26, 2021, 10, 'MANUTENCAO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('IMK4O22', 7, 5, 9, 26, 2023, 6, 'MANUTENCAO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UGG3T45', 3, 2, 9, 27, 2025, 6, 'MANUTENCAO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YLR7S96', 9, 3, 7, 19, 2023, 2, 'DISPONIVEL', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('EBV0G42', 5, 3, 3, 7, 2019, 4, 'MANUTENCAO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('LOD9V83', 9, 4, 4, 12, 2019, 1, 'ALUGADO', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NZV5R00', 5, 2, 10, 29, 2018, 6, 'MANUTENCAO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('XTG3J72', 2, 2, 3, 9, 2023, 2, 'ALUGADO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NIR4E29', 5, 1, 6, 17, 2017, 1, 'DISPONIVEL', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('TBY9V47', 10, 5, 6, 16, 2020, 9, 'DISPONIVEL', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('QCZ8F68', 9, 4, 2, 5, 2018, 4, 'MANUTENCAO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('KLJ3Y98', 8, 5, 1, 1, 2025, 6, 'ALUGADO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UTT3B56', 2, 4, 5, 15, 2019, 1, 'MANUTENCAO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('QZQ7P04', 4, 3, 9, 25, 2016, 3, 'MANUTENCAO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('AUC3V36', 2, 2, 9, 27, 2021, 2, 'MANUTENCAO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('AOW5B11', 1, 2, 5, 15, 2016, 10, 'DISPONIVEL', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('NMO6N50', 7, 1, 9, 25, 2025, 10, 'DISPONIVEL', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BWC5Q14', 5, 5, 2, 5, 2022, 7, 'ALUGADO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('PFR3E68', 5, 2, 5, 15, 2025, 6, 'DISPONIVEL', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('RED0A99', 7, 5, 9, 25, 2019, 4, 'MANUTENCAO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('GNU8E22', 4, 5, 5, 13, 2016, 3, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BYR7U81', 5, 1, 5, 13, 2018, 10, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('LEV3D49', 2, 2, 6, 17, 2022, 6, 'MANUTENCAO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('WSE9S55', 7, 2, 1, 2, 2018, 4, 'MANUTENCAO', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ILE5P77', 6, 3, 8, 24, 2016, 3, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('CIE8G41', 2, 1, 9, 27, 2015, 10, 'MANUTENCAO', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('YSG0R48', 9, 4, 3, 8, 2021, 4, 'MANUTENCAO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('EOQ8C65', 9, 1, 4, 12, 2025, 3, 'MANUTENCAO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('OFF9S27', 6, 1, 4, 11, 2018, 2, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('HBV3Q56', 8, 4, 1, 1, 2025, 6, 'DISPONIVEL', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UIX4S89', 3, 4, 7, 20, 2024, 2, 'MANUTENCAO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('MHQ6L57', 8, 5, 1, 3, 2017, 8, 'DISPONIVEL', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('CQU4G22', 3, 3, 10, 30, 2016, 4, 'ALUGADO', 'Excelente desempenho e ótimo custo-benefício.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('XDP7P39', 3, 4, 1, 1, 2015, 3, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('FEU8B92', 1, 2, 5, 13, 2023, 7, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('UAX4C37', 10, 4, 8, 22, 2018, 7, 'MANUTENCAO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ZSY2T84', 3, 3, 8, 24, 2024, 2, 'DISPONIVEL', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('MED0G88', 7, 2, 10, 28, 2017, 8, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BZR8Y11', 8, 2, 10, 30, 2015, 7, 'MANUTENCAO', 'Veículo seminovo, único dono.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('LBW8N37', 7, 3, 10, 30, 2024, 2, 'ALUGADO', 'Revisão completa feita na concessionária.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('IPY3C49', 9, 4, 5, 13, 2015, 1, 'MANUTENCAO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('DQA6U35', 7, 3, 2, 4, 2021, 10, 'ALUGADO', 'Veículo com todas as manutenções em dia.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('RNS5T46', 4, 4, 9, 27, 2022, 3, 'ALUGADO', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('RPI4S56', 3, 1, 4, 11, 2017, 2, 'DISPONIVEL', 'Interior limpo e bem cuidado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('MZK8T02', 7, 1, 2, 4, 2022, 10, 'ALUGADO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('CIH5C90', 7, 4, 10, 30, 2023, 5, 'MANUTENCAO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('BYF7O03', 9, 3, 4, 10, 2024, 9, 'MANUTENCAO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('TFV0L30', 3, 1, 5, 14, 2020, 7, 'ALUGADO', 'Carro pronto para uso imediato.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('GQA1C84', 9, 5, 6, 17, 2019, 4, 'MANUTENCAO', 'Carro econômico e confortável.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('OMS0I26', 9, 1, 9, 25, 2023, 5, 'ALUGADO', 'Baixa quilometragem e ótimo estado de conservação.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('ZQI8Z94', 8, 4, 10, 29, 2018, 6, 'ALUGADO', 'Pneus novos e motor revisado.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('SAP1I16', 4, 2, 6, 17, 2020, 9, 'MANUTENCAO', 'Veículo bem conservado e revisado recentemente.');
INSERT INTO vehicle (placa, id_categoria, id_seguro, id_brand, id_modelo, ano, id_cor, status, descricao) VALUES ('WIH4C65', 2, 5, 3, 8, 2020, 6, 'DISPONIVEL', 'Carro pronto para uso imediato.');