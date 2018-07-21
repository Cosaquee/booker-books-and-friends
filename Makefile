run:
	lein ring server

lint:
	lein cljfmt check

fix:
	lein cljfmt fix

docker:
	docker run --publish=7474:7474 --publish=7687:7687 --name neo4j -d  neo4j:3.0