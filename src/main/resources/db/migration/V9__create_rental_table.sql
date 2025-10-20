CREATE TABLE rental (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_placa VARCHAR(10) NOT NULL,
    customer_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    price DECIMAL(10,2),
    returned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_placa) REFERENCES vehicle(placa),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
)