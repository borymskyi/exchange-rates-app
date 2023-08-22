#!/bin/bash

IMAGE_NAME=dimoks/exchange-rates-app-image

pushd ../../

if [[ -f .env ]]; then
  source .env
  export APP_PORT
  export PROVIDER_AVCURRENCY_URL
  export PROVIDER_AVEXCHANGERATE_URL
  export PROVIDER_AVHISTORIC_URL
  export PROVIDER_V6EXCHANGERATE_URL
  export REDIS_PORT
  export REDIS_PASSWORD
else
  echo "----- file .env not found! -----"
fi

mvn clean install -DskipTests=false \
  -DAPP_PORT="$APP_PORT" \
  -DPROVIDER_AVCURRENCY_URL="$PROVIDER_AVCURRENCY_URL" \
  -DPROVIDER_AVEXCHANGERATE_URL="$PROVIDER_AVEXCHANGERATE_URL" \
  -DPROVIDER_AVHISTORIC_URL="$PROVIDER_AVHISTORIC_URL" \
  -DPROVIDER_V6EXCHANGERATE_URL="$PROVIDER_V6EXCHANGERATE_URL" \
  -DREDIS_PORT="$REDIS_PORT" \
  -DREDIS_PASSWORD="$REDIS_PASSWORD"

docker build -t $IMAGE_NAME .

docker image ls | grep $IMAGE_NAME