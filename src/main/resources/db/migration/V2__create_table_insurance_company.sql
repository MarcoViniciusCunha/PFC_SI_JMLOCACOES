CREATE TABLE insurance_company (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(255)
);

INSERT INTO insurance_company (name, contact) VALUES
('Porto Seguro', '0800 727 0800'),
('SulAmérica Seguros', '4004 4100'),
('Allianz Seguros', '0800 777 7243'),
('Bradesco Seguros', '0800 727 9966'),
('Mapfre Seguros', '0800 775 4545'),
('Tokio Marine', '0300 33 86546'),
('HDI Seguros', '0800 770 1608'),
('Sompo Seguros', '0800 77 19 119');