---
services: cosmos-db
platforms: spring boot framework
author: mojind
---

# Introduction
Azure Cosmos DB is Microsoft’s globally distributed multi-model database service. You can quickly create and query document, key/value, and graph databases, all of which benefit from the global distribution and horizontal scale capabilities at the core of Azure Cosmos DB.

One of the supported APIs is the Cassandra API, which provides a document model and support for client drivers in many platforms. This sample shows you how to use the Azure Cosmos DB with the Cassandra DB API to store and access data from a Spring Boot Framework Application. It is transparent to the application that the data is stored in Azure Cosmos DB.

## Running this sample
* Before you can run this sample, you must have the following perquisites:
	* An active Azure Cassandra API account - If you don't have an account, refer to the [Create Cassandra API account](https://docs.microsoft.com/en-us/azure/cosmos-db/tutorial-develop-cassandra-java). 
	* JDK 1.8+ 
	* [Git](http://git-scm.com/).

1. Clone this repository using `git clone https://github.com/mojind/azure-cosmos-db-cassandra-db-spring-boot-tutorial`

2. Install any IDE like [Intellij](https://www.jetbrains.com/idea/download/#section=windows) for Java Application.

3. Next, configure the endpoints in **application.properties**
```
	spring.data.cassandra.contact-points=<FILL ME>
	spring.data.cassandra.username=<FILL ME>
	spring.data.cassandra.password=<FILL ME>
```
 
4. The main method is in **CassandraCRUDTest** java class. The keyspace and table creation dynamically are handled through SpringDataCassandraConfiguration.

## About the code
1. The code included in this sample is intended to get you started with a Java application that connects to Azure Cosmos DB with Cassandra API. It mainly performs CRUD operations on Cassandra.

2. Cassandra Table used is **Race**. This is modeled as Race Class. This has **RacePK** class which models the primary key for the race table. This has 2 partition Keys (race_year, race_name) and one clustering key (rank) which are in RacePK class. Combination of Partition Key and Clustering key makes a primary key on table. Race Class has other parameters like cyclist_name, location, audiance_capacity, any_accident, ticket_price, transaction_id, shop_ip spanning multiple data types like string, int, decimal, inet, UUID etc.

> **Create**: The insertion of new records is in InsertQueries where we insert data into the race table.

> **Read**: The reading of records is through SelectQueries. Here various type of operation is performed spanning various combinations of where clause, limit clause,  order by clause, logical operators and partition key like Not given, partially given and completely specified.

> **Update**: The update of records is through UpdateQueries which updates a record only if complete primary key is given, else proper error message is thrown.

> **Delete**: The deletion of records is through DeleteQueries which deletes a record only if complete partition key is given, else proper error message is thrown.

The queries used in above class are modeled in **RaceRepository** Class. 

## More information

- [Azure Cosmos DB](https://docs.microsoft.com/azure/cosmos-db/introduction)
- [Cassandra DB](http://cassandra.apache.org/)
