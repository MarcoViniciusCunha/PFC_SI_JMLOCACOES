CREATE TABLE inspection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rental_id BIGINT NOT NULL,
    data_inspecao DATE NOT NULL,
    descricao TEXT,
    danificado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (rental_id) REFERENCES rental(id)
);
