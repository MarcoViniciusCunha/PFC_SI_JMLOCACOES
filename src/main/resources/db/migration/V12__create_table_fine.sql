CREATE TABLE fine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa_veiculo VARCHAR(10),
    valor DECIMAL(10, 2),
    location_id INT,
    dias_atraso INT,
    FOREIGN KEY (location_id) REFERENCES location(id)
);
