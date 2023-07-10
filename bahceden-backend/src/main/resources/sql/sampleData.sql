USE bahceden;

-- Insert data into the "producers" table
INSERT INTO producers (name, shop_name, email, phone_number)
VALUES ('John Doe', 'Doe Shop', 'john.doe@example.com', '1234567890');

INSERT INTO producers (name, shop_name, email, phone_number)
VALUES ('Jane Smith', 'Smith Shop', 'jane.smith@example.com', '9876543210');



-- Insert data into the "customers" table
INSERT INTO customers (name, email)
VALUES ('Alice Johnson', 'alice.johnson@example.com');

INSERT INTO customers (name, email)
VALUES ('Bob Williams', 'bob.williams@example.com');


-- Insert data into the "categories" table
INSERT INTO categories (name)
VALUES ('Dairy');

INSERT INTO categories (name)
VALUES ('Vegetable');

INSERT INTO categories (name)
VALUES ('Meat');

INSERT INTO categories (name)
VALUES ('Nuts');

INSERT INTO categories (name)
VALUES ('Bakery');

INSERT INTO categories (name)
VALUES ('Other');


-- Insert data into the "products" table
INSERT INTO products (producer_id, category_id, sub_category_id, name, description, unit_type, available_amount, price_per_unit)
VALUES (1, 2, 3, 'Laptop 1', 'Powerful laptop for professional use', 1, 10, 1500.00);

INSERT INTO products (producer_id, category_id, sub_category_id, name, description, unit_type, available_amount, price_per_unit)
VALUES (2, 2, 3, 'Laptop 2', 'Compact and lightweight laptop', 1, 5, 1200.00);



-- Insert data into the "favorite_products" table
INSERT INTO favorite_products (customer_id, product_id)
VALUES (1, 1);

INSERT INTO favorite_products (customer_id, product_id)
VALUES (2, 2);


-- Insert data into the "favorite_producers" table
INSERT INTO favorite_producers (customer_id, producer_id)
VALUES (1, 1);

INSERT INTO favorite_producers (customer_id, producer_id)
VALUES (2, 2);


-- Insert data into the "addresses" table
INSERT INTO addresses (title, full_address, phone_number, customer_id)
VALUES ('Home', '123 Main St, City, Country', '1234567890', 1);

INSERT INTO addresses (title, full_address, phone_number, customer_id)
VALUES ('Work', '456 Business Rd, City, Country', '9876543210', 2);



-- Insert data into the "orders" table
INSERT INTO orders (customer_id, producer_id, product_id, status, amount, date_of_purchase, shipment_type, delivery_address_id)
VALUES (1, 1, 1, 1, 2, '2023-05-01', 1, 1);

INSERT INTO orders (customer_id, producer_id, product_id, status, amount, date_of_purchase, shipment_type, delivery_address_id)
VALUES (2, 2, 2, 1, 1, '2023-05-02', 2, 2);


-- Insert data into the "comments" table
INSERT INTO comments (message, product_id, author_id, parent_id, count_of_likes)
VALUES ('Great product!', 1, 1, NULL, 10);

INSERT INTO comments (message, product_id, author_id, parent_id, count_of_likes)
VALUES ('Excellent service!', 2, 2, NULL, 5);


-- Insert data into the "scraped_data" table
INSERT INTO scraped_data (name, unit, min_price, max_price, category_id)
VALUES ('Product A', 1, 10.00, 20.00, 1);

INSERT INTO scraped_data (name, unit, min_price, max_price, category_id)
VALUES ('Product B', 2, 5.00, 10.00, 1);


-- Insert data into the "liked_comments" table
INSERT INTO liked_comments (customer_id, comment_id)
VALUES (1, 1);

INSERT INTO liked_comments (customer_id, comment_id)
VALUES (2, 2);