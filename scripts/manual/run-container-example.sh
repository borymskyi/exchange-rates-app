#!/bin/bash

IMAGE_KEY="exchange-rates-app"
IMAGE_NAME="dimoks/$IMAGE_KEY-image"
CONTAINER_NAME="exchange-rates-app"
PORT=8080

docker container stop $CONTAINER_NAME
docker container rm $CONTAINER_NAME
docker run --name $CONTAINER_NAME \
  -p $PORT:$PORT \
  -e "JAVA_OPTS=-DAPP_PORT=\
    -DSENTRY_DSN= \
    -DPROVIDER_AVCURRENCY_URL= \
    -DPROVIDER_AVEXCHANGERATE_URL= \
    -DPROVIDER_AVHISTORIC_URL= \
    -DPROVIDER_V6EXCHANGERATE_URL=" \
  -d $IMAGE_NAME