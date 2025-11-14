CREATE TABLE insurance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empresa VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    validade DATE NOT NULL
);

INSERT INTO insurance (empresa, valor, validade) VALUES
('Porto Seguro', 1899.90, '2026-11-01'),
('SulAmérica Seguros', 1620.50, '2026-08-15'),
('Bradesco Seguros', 1745.75, '2026-09-20'),
('Allianz Seguros', 1980.00, '2026-12-10'),
('Tokio Marine Seguradora', 1550.30, '2026-07-25');
