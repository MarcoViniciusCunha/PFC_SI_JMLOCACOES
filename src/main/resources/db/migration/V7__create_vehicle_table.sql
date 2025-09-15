CREATE TABLE vehicle (
    placa VARCHAR(10) PRIMARY KEY,
    id_categoria INT,
    id_seguro INT,
    id_mark INT,
    modelo VARCHAR(255),
    ano INT,
    cor VARCHAR(20),
    status VARCHAR(20) DEFAULT 'DISPONIVEL',
    descricao TEXT,
    FOREIGN KEY (id_categoria) REFERENCES category(id),
    FOREIGN KEY (id_seguro) REFERENCES insurance(id),
    FOREIGN KEY (id_mark) REFERENCES mark(id)
);