# Bahceden App Setup Guide
Welcome to the Bahceden application setup guide! This guide will walk you through how to get your local development environment set up.

### Prerequisites
Before we begin, make sure you have the following tools installed on your system:

Android Studio
MySQL Workbench
MySQL Server
Java Development Kit (JDK)

### Steps to Set Up
#### 1. MySQL Database Setup
Launch MySQL Workbench and establish a new connection to your MySQL Server:

Click the '+' symbol next to 'MySQL Connections'.
Name your connection 'bahceden'.
Enter the username and password you set in your backend's 'application.properties' file.
Click 'Test Connection' to ensure everything is set up correctly.
If the connection is successful, click 'OK' to save the connection.
Once the connection is established, open the SQL scripts located in the 'res' folder and execute them in the order below. This will set up the necessary tables for your application.

createDB.sql > createUser.sql > createTables.sql

After successfully importing the project, navigate to the 'bahceden-backend\src\main\resourcesapplication.properties' file. Update the MySQL username and password as per your MySQL server setup.
spring.datasource.username=\<your-username>
spring.datasource.password=\<your-password>

#### 2. Spring Boot Backend Setup

Open terminal in "bahceden-backend" and run the lines below in order.

```sh
./mvnw package 
./mvnw spring-boot:run 
```

#### 3. Android Frontend Setup
Clone the repository and open the project in Android Studio:
Once you have the project open, you should be able to run the Android application on your device or emulator.