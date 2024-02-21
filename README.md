# Service Provider App
# Backend

To run the Backend locally, a Postgres database needs to be set-up with parameters like defined in `docker-compose.yaml`.
The simplest way is to run Postgres in a docker container, and then start the app locally. Upon starting the app,
Flyway will make sure the schema is up-to-date.