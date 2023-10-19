#!/bin/bash
ssl_path=$HOME/software/nebula/ssl
if [ ! -d "$ssl_path" ]; then
    mkdir -p $ssl_path
    cp ../ssl/* $ssl_path
fi
docker-compose up -d
