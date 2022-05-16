#!/bin/bash
data_dir=$HOME/software/postgres/doobie/data
conf_dir=$HOME/software/postgres/doobie/conf
if [ ! -d "$data_dir" ]; then
  mkdir -p $data_dir
fi
if [ ! -d "$conf_dir" ]; then
  mkdir -p $conf_dir
fi
if [ ! -e "$conf_dir/postgres.conf" ]; then
    cp postgres.conf $conf_dir/
fi
if [ ! -e "$conf_dir/postgres-passwd" ]; then
    cp postgres-passwd $conf_dir/
fi
docker run -d --name doobie-postgres -h doobie-postgres \
           -e POSTGRES_USER=postgres \
           -e POSTGRES_PASSWORD_FILE=/run/secrets/postgres-passwd \
           -e PGDATA=/var/lib/postgresql/data/pgdata \
           -v $data_dir:/var/lib/postgresql/data \
           -v $conf_dir/postgres.conf:/etc/postgresql/postgresql.conf \
           -v $conf_dir/postgres-passwd:/run/secrets/postgres-passwd \
           -p 5432:5432 \
           postgres -c 'config_file=/etc/postgresql/postgresql.conf'
