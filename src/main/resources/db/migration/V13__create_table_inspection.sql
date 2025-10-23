CREATE TABLE inspection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    fine_id INT,
    payment_id INT,
    status VARCHAR(50),
    valor_total DECIMAL(10, 2),
    FOREIGN KEY (location_id) REFERENCES location(id),
    FOREIGN KEY (fine_id) REFERENCES fine(id),
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);
