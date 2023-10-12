#!/bin/bash
data_dir=$HOME/software/neo4j/neotypes/data
logs_dir=$HOME/software/neo4j/neotypes/logs
import_dir=$HOME/software/neo4j/neotypes/import
if [ ! -d "$data_dir" ]; then
    mkdir -p $data_dir
fi
if [ ! -d "$logs_dir" ]; then
    mkdir -p $logs_dir
fi
if [ ! -d "$import_dir" ]; then
    mkdir -p $import_dir
fi
docker run -d --name neotypes-neo4j -h neotypes-neo4j \
           -e "NEO4J_AUTH=neo4j/neotypes" \
           -e NEO4J_server_directories_import=import \
           -e NEO4J_apoc_export_file_enabled=true \
           -e NEO4J_apoc_import_file_enabled=true \
           -e NEO4J_apoc_import_file_use__neo4j__config=true \
           -e NEO4J_PLUGINS=\[\"apoc\"\] \
           -v $data_dir:/data \
           -v $logs_dir:/logs \
           -v $import_dir:/var/lib/neo4j/import \
           -p 7474:7474 -p 7687:7687 \
           neo4j:5.12.0-community
