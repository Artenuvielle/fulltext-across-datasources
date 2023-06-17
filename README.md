# fulltext-across-datasources

Demo project for executing a full text search over data coming from two different data sources while still maintaining
the ability for sorting and pagination.
 
This repository consists of two parts:
 - a [Spring Boot backend](https://github.com/Artenuvielle/fulltext-across-datasources/tree/master/spring-boot-backend) connecting to two postgresql databases (one containing many patient data and one containing the other containing addresses for some patients) and an Elasticsearch container for indexing said data
 - an [Angular frontend](https://github.com/Artenuvielle/fulltext-across-datasources/tree/master/angular-frontend) displaying a table with all patient data connected with their addresses

For starting the whole system download the contents of this repository and run:

```docker compose up```

This will start:
 - the backend running on port 8080
 - the frontend running on port 4200
 - the two postgresql databases running on ports 5432 and 5433 respectively
 - the Elasticsearch container running on port 9200 and 9300
