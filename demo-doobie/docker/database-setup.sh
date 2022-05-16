docker exec -it doobie-postgres psql -c 'create database world;' -U postgres
docker cp world.sql doobie-postgres:/
docker exec -it doobie-postgres psql -c '\i world.sql' -d world -U postgres
docker exec -it doobie-postgres psql -d world -c "create type myenum as enum ('foo', 'bar')" -U postgres
