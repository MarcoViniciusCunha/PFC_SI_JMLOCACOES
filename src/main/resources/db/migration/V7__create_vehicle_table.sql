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