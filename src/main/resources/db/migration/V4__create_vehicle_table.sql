CREATE TABLE vehicle (
    placa VARCHAR(10) PRIMARY KEY,
    id_categoria INT,
    id_seguro INT,
    marca VARCHAR(255),
    modelo VARCHAR(255),
    ano INT,
    cor VARCHAR(20),
    status VARCHAR(50),
    descricao TEXT,
    FOREIGN KEY (id_categoria) REFERENCES Category(id),
    FOREIGN KEY (id_seguro) REFERENCES Insurence(id)
);