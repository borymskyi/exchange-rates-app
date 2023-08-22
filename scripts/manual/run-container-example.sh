#!/bin/bash

IMAGE_KEY="exchange-rates-app"
IMAGE_NAME="dimoks/$IMAGE_KEY-image"
CONTAINER_NAME="exchange-rates-app"

docker container stop $CONTAINER_NAME
docker container rm $CONTAINER_NAME
docker run --name $CONTAINER_NAME \
  -p $APP_PORT:$APP_PORT \
  -e "JAVA_OPTS=-DAPP_PORT=\
    -DPROVIDER_AVCURRENCY_URL= \
    -DPROVIDER_AVEXCHANGERATE_URL= \
    -DPROVIDER_AVHISTORIC_URL= \
    -DPROVIDER_V6EXCHANGERATE_URL= \
    -DREDIS_PORT= \
    -DREDIS_PASSWORD=" \
  -d $IMAGE_NAME