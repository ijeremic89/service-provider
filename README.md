# Service Provider App
CRUD app with two entities (Provider and Service) with many-to-many relation

# Backend Setup
Be sure to have Java 17 and maven installed on your machine:
Open terminal and put:
mvn -version // for getting installed maven version

Service Provider App using POSTGRES for storing data. 

To run the Backend locally, a Postgres database needs to be set-up with parameters like defined in `docker-compose.yaml`.
The simplest way is to run Postgres in a docker container, and then start the app locally. Upon starting the app,
Flyway will make sure the schema is up-to-date.

# Tests
To run tests locally run command mvn test