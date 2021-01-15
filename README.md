
## Table of contents
* [General info](#general-info)
* [Setup](#setup)
* [Technologies](#technologies)
* [Frontend](#frontend)

## General info
![Alt text](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/backend.png?raw=true "Backend")

The backend is written in Java using Spring Boot and Spring Cloud technologies using the Kubernetes platform.
Using a web browser both from a computer and from a mobile device, we communicate with the Gateway Server, which is the only one with an IP address available from outside. We improve security by preventing the user from direct communication with modules and all network traffic is sent through the gate. Each microservice can work independently of others and have its own database, which gives great flexibility when choosing the type of database.

## Setup
 1. Requires before
     - Docker desktop [tutorial](https://docs.docker.com/docker-for-windows/install/)
     - Maven [tutorial](https://maven.apache.org/download.cgi)
     
 2. Make execute jar serwer files
Go to main folder and type in console
```
mvn clean install -DskipTests=true
```
 3. Run Apache Kafka 
 Need runned docker.
 Go to main folder (where is docker-compose.yml) and type in console
 ```
 docker-compose up -d
 ```
 When everything is ok it should be looks like 
 ![Alt text](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/obraz_2021-01-15_225641.png?raw=true "docker")
 
 4. Run serwers locally
 Go to main folder and type for example
 ```
 java -jar .\config-server\target\config-server.jar
 ```
 
 Repeat this for all microservices.
 
 Run priority list > config-server >> other 
 config serwer share configs to other microservices

## Technologies
Project is created with:
 1. Java 11
 2. Spring Boot
 3. Spring Cloud
 4. Apache Kafka
 5. PostgreSQL
 
## Frontend
[React](https://github.com/ArturoS159/RestaurantManagment_Front)
