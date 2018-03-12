# booker-books

Clojure service with Neo4j as database. Part of Booker responsible for books and authors.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

You will also need Neo4j database running on localhost.

`docker run --publish=7474:7474 --publish=7687:7687 neo4j:3.0`

Remeber that you need to login and change password first.

## Fixtures
You can find all test data in `database/fixtures.neo`. Test data is in polish(due to the fact that I own only polish books).

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2018 Karol Kozakowski
