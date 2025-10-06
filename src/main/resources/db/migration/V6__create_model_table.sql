CREATE TABLE model (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    brand_id INT NOT NULL,
    CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brand(id)
);

-- Volkswagen
INSERT INTO model (nome, brand_id) VALUES
('Gol', 1),
('Polo', 1),
('Golf', 1);

-- Fiat
INSERT INTO model (nome, brand_id) VALUES
('Uno', 2),
('Argo', 2),
('Cronos', 2);

-- Chevrolet
INSERT INTO model (nome, brand_id) VALUES
('Onix', 3),
('Prisma', 3),
('Tracker', 3);

-- Toyota
INSERT INTO model (nome, brand_id) VALUES
('Corolla', 4),
('Yaris', 4),
('Hilux', 4);

-- Hyundai
INSERT INTO model (nome, brand_id) VALUES
('HB20', 5),
('Creta', 5),
('Tucson', 5);

-- Ford
INSERT INTO model (nome, brand_id) VALUES
('Ka', 6),
('EcoSport', 6),
('Ranger', 6);

-- Renault
INSERT INTO model (nome, brand_id) VALUES
('Kwid', 7),
('Sandero', 7),
('Duster', 7);

-- Honda
INSERT INTO model (nome, brand_id) VALUES
('Civic', 8),
('City', 8),
('HR-V', 8);

-- Nissan
INSERT INTO model (nome, brand_id) VALUES
('March', 9),
('Versa', 9),
('Kicks', 9);

-- Jeep
INSERT INTO model (nome, brand_id) VALUES
('Renegade', 10),
('Compass', 10),
('Wrangler', 10);