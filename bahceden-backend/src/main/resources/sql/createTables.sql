USE bahceden;

DROP TABLE IF EXISTS producers;
CREATE TABLE producers (
                           id INT AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           shop_name VARCHAR(255),
                           email VARCHAR(255) NOT NULL,
                           phone_number VARCHAR(255),
                           profile_image_url VARCHAR(255) DEFAULT "http://localhost:8080/images/noProfile.png",
                           background_image_url VARCHAR(255) DEFAULT "http://localhost:8080/images/plainWhite.jpg",
                           rating DECIMAL(2,1) DEFAULT 0.0,
                           PRIMARY KEY (id)
);

DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
                           id INT AUTO_INCREMENT,
                           name VARCHAR(255),
                           email VARCHAR(255) NOT NULL,
                           profile_image_url VARCHAR(255) DEFAULT "http://localhost:8080/images/noProfile.png",
                           PRIMARY KEY (id)
);

DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
                            id INT AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            parent_id INT DEFAULT NULL,
                            PRIMARY KEY (id),
                            FOREIGN KEY (parent_id) REFERENCES categories(id)
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
                          id INT AUTO_INCREMENT,
                          producer_id INT NOT NULL,
                          category_id INT NOT NULL,
                          sub_category_id INT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          unit_type INT NOT NULL,
                          available_amount INT NOT NULL,
                          price_per_unit DECIMAL(10,2),
                          image_url VARCHAR(255) DEFAULT "http://localhost:8080/images/plainWhite.jpg",
                          rating DECIMAL(2,1) DEFAULT 0.0,
                          PRIMARY KEY (id),
                          FOREIGN KEY (producer_id) REFERENCES producers(id) ON DELETE CASCADE,
                          FOREIGN KEY (category_id) REFERENCES categories(id)
);

DROP TABLE IF EXISTS favorite_products;
CREATE TABLE favorite_products (
                                   id INT AUTO_INCREMENT,
                                   customer_id INT NOT NULL,
                                   product_id INT NOT NULL,
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                                   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS favorite_producers;
CREATE TABLE favorite_producers (
                                    id INT AUTO_INCREMENT,
                                    customer_id INT NOT NULL,
                                    producer_id INT NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                                    FOREIGN KEY (producer_id) REFERENCES producers(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
                           id INT AUTO_INCREMENT,
                           title VARCHAR(255),
                           full_address TEXT NOT NULL,
                           phone_number VARCHAR(255),
                           customer_id INT,
                           PRIMARY KEY (id),
                           FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
                        id INT AUTO_INCREMENT,
                        customer_id INT NOT NULL,
                        producer_id INT NOT NULL,
                        product_id INT NOT NULL,
                        status INT,
                        amount INT,
                        date_of_purchase DATE,
                        shipment_type INT,
                        delivery_address_id INT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (customer_id) REFERENCES customers(id),
                        FOREIGN KEY (producer_id) REFERENCES producers(id),
                        FOREIGN KEY (product_id) REFERENCES products(id),
                        FOREIGN KEY (delivery_address_id) REFERENCES addresses(id)
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments (
                          id INT AUTO_INCREMENT,
                          message TEXT NOT NULL,
                          product_id INT NOT NULL,
                          author_id INT NOT NULL,
                          parent_id INT,
                          count_of_likes INT DEFAULT 0,
                          rating DECIMAL(2,1) DEFAULT 0.0,
                          PRIMARY KEY (id),
                          FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                          FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE
);


DROP TABLE IF EXISTS scraped_data;
CREATE TABLE scraped_data (
                              id INT AUTO_INCREMENT,
                              name VARCHAR(255),
                              unit INT,
                              min_price DECIMAL(10, 2),
                              max_price DECIMAL(10, 2),
                              category_id INT,
                              PRIMARY KEY (id),
                              FOREIGN KEY (category_id) REFERENCES categories(id)
);

DROP TABLE IF EXISTS liked_comments;
CREATE TABLE liked_comments (
                                   id INT AUTO_INCREMENT,
                                   customer_id INT NOT NULL,
                                   comment_id INT NOT NULL,
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                                   FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);