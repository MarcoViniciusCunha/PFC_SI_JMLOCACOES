CREATE TABLE brand (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);
INSERT INTO brand (nome) VALUES
('Volkswagen'),
('Fiat'),
('Chevrolet'),
('Toyota'),
('Hyundai'),
('Ford'),
('Renault'),
('Honda'),
('Nissan'),
('Jeep');