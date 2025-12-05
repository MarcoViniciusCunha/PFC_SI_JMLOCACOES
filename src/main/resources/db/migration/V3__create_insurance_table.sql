CREATE TABLE insurance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    validade DATE NOT NULL,
    CONSTRAINT fk_insurance_company
        FOREIGN KEY (company_id)
        REFERENCES insurance_company (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

INSERT INTO insurance (company_id, valor, validade) VALUES
(1, 1299.90, '2026-05-15'),
(1, 1599.00, '2026-09-30'),
(2, 1390.50, '2026-03-20'),
(2, 990.00,  '2025-12-31'),
(3, 1799.99, '2026-07-10'),
(3, 850.00,  '2025-11-15');
