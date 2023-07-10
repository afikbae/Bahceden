-- Drop user first if they exist
DROP USER if exists 'bahcedenDB'@'localhost' ;

-- Now create user with prop privileges
CREATE USER 'bahcedenDB'@'localhost' IDENTIFIED BY 'bahcedenDB';

GRANT ALL PRIVILEGES ON * . * TO 'bahcedenDB'@'localhost';