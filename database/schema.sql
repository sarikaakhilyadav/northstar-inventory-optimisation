-- NorthStar Inventory Optimisation Platform
-- Database schema setup for MySQL 8

CREATE DATABASE IF NOT EXISTS northstar_inventory;

USE northstar_inventory;

CREATE TABLE IF NOT EXISTS products (
    product_id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    cost_price DOUBLE NOT NULL,
    selling_price DOUBLE NOT NULL,
    current_stock INT NOT NULL,
    reorder_level INT NOT NULL,
    units_sold INT NOT NULL,
    PRIMARY KEY (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
