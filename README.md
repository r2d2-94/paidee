# paidee(`ee` from `y` is intentional)
This is simple Client Server Application written in Scala. The application tries to simulate waiters in a restaurant sending food orders from customers to a backend service , which persists the data for each table and provides APIs to add / delete items from a table, as well as querying open orders. 

The application requires a postgres instance runnnig as a persistant storage at localhost on port 5432 with database name `postgres`. There are configurations for port and authentication provided in `reference.conf`. For integration tests... it expects a database names `test` to be present.

Frameworks used :
- http4s with Blaze Client and Server Builder.
- doobie for database
- Cats for functional

#How to run
- Ensure postgres database instance up
- Execute `run` from sbt shell or `sbt run` in the home directory of this project.
- A Client is started with the server.
- Use PostMan or any other client to view current status or add/delete items to a table

