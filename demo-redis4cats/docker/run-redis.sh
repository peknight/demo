#!/bin/bash
data_dir=$HOME/software/redis/redis4cats/data
conf_dir=$HOME/software/redis/redis4cats/conf
if [ ! -d "$data_dir" ]; then
    mkdir -p $data_dir
fi
if [ ! -d "$conf_dir" ]; then
    mkdir -p $conf_dir
fi
if [ ! -e "$conf_dif/redis.conf" ]; then
    cp redis.conf $conf_dir/
fi
docker run -d --name redis4cats-redis -h redis4cats-redis \
           -v $data_dir:/data \
           -v $conf_dir/redis.conf:/usr/local/etc/redis/redis.conf \
           -p 6379:6379 \
           redis redis-server /usr/local/etc/redis/redis.conf
