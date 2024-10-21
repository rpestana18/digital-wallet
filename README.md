# Digital Wallet
A digital wallet project created to demonstrate a creation of a Wallet microservice made in Java with springboot using an H2 database (in memory) to simulate real execution and run functional tests.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white) 

### Functionalities
● Create Wallet: creation of wallets for customers.

● Retrieve Balance: retrieve the current balance of a user's wallet.

● Deposit Funds: Enable users to deposit money into their wallets.

● Withdraw Funds: Enable users to withdraw money from their wallets.

● Transfer Funds: Facilitate the transfer of money between user wallets.

● Retrieve Historical Balance: retrieve the balance of a user's wallet at a specific
point in the past. (in progress)

## Technologies

### Language
As the requirement of this challenge, this project was developed in java.
The version used was the Java 17 in order to resolve supported problems.

#### Dependencies
#### 1. spring-boot-starter-data-jpa - Spring Boot Starter Data JPA - to integrate with the database
#### 2. Junit 5 - framework used to write unit tests
#### 3. Mockito - framework used to create mock objects to use in our tests
#### 4. H2 Database - database used only for tests.
#### 5. JUnit Jupiter - API for writing tests using JUnit 5

### Database
IN this project I'm using H2 database in order to execute tests during the runtime only.
Ina real case we need to ensure all functional and non-functional requirements to choose the best solution.

There are two tables for this service: WALLET and TRANSACTION.
1. The table WALLET will maintain all needed wallet information.
2. The table TRANSACTION will register the log of Operations executed to apply the requirement of full traceability of
all operations in order to facilitate auditing of wallet balances.

For this testcase, we're just considering a relational database because the objective is register the process.
Is important to say that in a real scenario, depending on the number of the transactions per second, the availability,
response time and scaling, we can consider relational database consolidated or a NoSql Database to ensure the performance.

### Database Access
To access the database, follow these steps:
1. start the Application;
2. open the H2 Database Interface with the url http://{your_host}/h2-console/login.jsp
3. JDBC URL: jdbc:h2:mem:walletserviceassignmentdb
4. user: root
5. password (no password only for tests)

## Tests
There are unit tests for all methods present on the Service Class. 
There also a Postman Collection prepared to execute all functional requirements.

## Architecture
This service was created to make part of a microservice architecture.
Based on Domain Driven Design, I used the separation of concern, concentrating in this service only the needed information to maintain a wallet.
Information of external services as Users or any other were not considered. 
This service only uses the necessary (external id) to generate a link with the wallet and the external entity that request and makes the owner of a wallet.
This service was constructed with some traditional concepts as the use of controller -> services and repositories. There is no interface to apply essencially the MVC model, but this is a secure structure to expose our endpoints.
This microservice receives synchronous requests, but it is totally adaptable to change for an asynchornous integration.

Microservice Patterns used: API Gateway, Access token, Domain-specific protocol, Domain event, Audit loggin, Database per service

## Non-functional Requirements

Some additional resources in order to resolve non-funcional requirementes:
1. Logs were included to help the auditory or a scenario of problem-solving;
2. Each request needs to have a x-trace-id in the header. It is a simple way to add the traceability of the request. Currently, the validation is only to check if the id exists, but there is flexibility to use.
3. Business Exception were created to separate functional problems of non-functional problems. All error messages are treated and shared with the client.
4. The resource file has the format yaml because this format has some advantages compared with .properties as flexibility.
5. I'm using the Spring Version 3.3.6. Because of it, in oder to resolve non-supportable resources, I'm using Java 17.

## Additional Information
Some functional requirements left scenarios open and, with the aim of avoiding exceptions, I made decisions that could all be revised, for example:

1. the customer can only have one wallet. In the text of the requirement, despite implying it, it did not state this. 
This could generate exceptions in features like retrieveBalance or retrieveHistoricalBalance that could return a list.

