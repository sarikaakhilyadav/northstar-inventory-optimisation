-- NorthStar Inventory Optimisation Platform
-- Sample inventory data for development and demonstration
-- Run schema.sql before this script.
-- Existing products with matching IDs or names are skipped.

USE northstar_inventory;

INSERT INTO products
(product_id, product_name, category, cost_price, selling_price, current_stock, reorder_level, units_sold)
SELECT sample.*
FROM (
    SELECT 1 AS product_id, 'Laptop' AS product_name, 'Electronics' AS category, 450 AS cost_price, 650 AS selling_price, 10 AS current_stock, 10 AS reorder_level, 45 AS units_sold
    UNION ALL SELECT 2, 'Wireless Mouse', 'Accessories', 10, 20, 25, 20, 156
    UNION ALL SELECT 3, 'Mechanical Keyboard', 'Accessories', 40, 80, 8, 10, 167
    UNION ALL SELECT 4, '24-inch Monitor', 'Electronics', 120, 180, 30, 10, 200
    UNION ALL SELECT 5, 'Office Printer', 'Office Equipment', 100, 160, 25, 10, 20
    UNION ALL SELECT 6, 'USB-C Hub', 'Accessories', 15, 35, 25, 15, 90
    UNION ALL SELECT 7, 'USB Wireless Adapter', 'Accessories', 8, 18, 40, 10, 0
) AS sample
WHERE NOT EXISTS (
    SELECT 1
    FROM products p
    WHERE p.product_id = sample.product_id
       OR p.product_name = sample.product_name
);
