CREATE TABLE payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    data DATE,
    valor DECIMAL(10, 2),
    forma_pagto VARCHAR(50),
    status VARCHAR(50),
    FOREIGN KEY (location_id) REFERENCES location(id)
);
