CREATE TABLE return_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    data_fim DATE,
    descr VARCHAR(255),
    FOREIGN KEY (location_id) REFERENCES location(id)
);
