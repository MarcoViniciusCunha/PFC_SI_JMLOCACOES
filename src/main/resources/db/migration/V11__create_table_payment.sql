CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rental_id BIGINT NOT NULL,
    data_pagamento DATE NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    forma_pagto VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    parcelas INT,
    descricao TEXT,
    FOREIGN KEY (rental_id) REFERENCES rental(id)
);
