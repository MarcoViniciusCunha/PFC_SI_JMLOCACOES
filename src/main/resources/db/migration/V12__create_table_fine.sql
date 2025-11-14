    CREATE TABLE fines (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        rental_id BIGINT NOT NULL,
        valor DECIMAL(10, 2),
        descricao TEXT,
        data_multa DATE NOT NULL,
        FOREIGN KEY (rental_id) REFERENCES rental(id)
    );
