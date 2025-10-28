CREATE TABLE location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnh_cliente VARCHAR(20) NOT NULL,
    placa_veiculo VARCHAR(10) NOT NULL,
    data_inicio DATE NOT NULL,
    data_est_fim DATE NOT NULL,
    valor_total DECIMAL(10, 2)
);
