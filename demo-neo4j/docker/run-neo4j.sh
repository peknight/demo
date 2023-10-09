#!/bin/bash
data_dir=$HOME/software/neo4j/neotypes/data
conf_dir=$HOME/software/neo4j/neotypes/conf
logs_dir=$HOME/software/neo4j/neotypes/logs
import_dir=$HOME/software/neo4j/neotypes/import
if [ ! -d "$data_dir" ]; then
    mkdir -p $data_dir
fi
if [ ! -d "$conf_dir" ]; then
    mkdir -p $conf_dir
fi
if [ ! -d "$logs_dir" ]; then
    mkdir -p $logs_dir
fi
if [ ! -d "$import_dir" ]; then
    mkdir -p $import_dir
fi
docker run -d --name neotypes-neo4j -h neotypes-neo4j \
           -e "NEO4J_AUTH=neo4j/neotypes" \
           -v $data_dir:/data \
           -v $logs_dir:/logs \
           -v $conf_dir:/var/lib/neo4j/conf \
           -v $import_dir:/var/lib/neo4j/import \
           -p 7474:7474 -p 7687:7687 \
           neo4j:5.12.0-community
