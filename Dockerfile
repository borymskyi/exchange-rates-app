FROM maven:3.9.0 AS maven-build

ARG PROVIDER_AVCURRENCY_URL
ARG PROVIDER_AVEXCHANGERATE_URL
ARG PROVIDER_AVHISTORIC_URL
ARG PROVIDER_V6EXCHANGERATE_URL
ARG REDIS_PORT
ARG REDIS_PASSWORD

WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests=false \
    -DPROVIDER_AVCURRENCY_URL="$PROVIDER_AVCURRENCY_URL" \
    -DPROVIDER_AVEXCHANGERATE_URL="$PROVIDER_AVEXCHANGERATE_URL" \
    -DPROVIDER_AVHISTORIC_URL="$PROVIDER_AVHISTORIC_URL" \
    -DPROVIDER_V6EXCHANGERATE_URL="$PROVIDER_V6EXCHANGERATE_URL" \
    -DREDIS_PORT="$REDIS_PORT" \
    -DREDIS_PASSWORD="$REDIS_PASSWORD"

FROM openjdk:11-jdk as app-build

ENV RELEASE=11

RUN groupadd --gid 1000 spring-app \
    && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app
USER spring-app:spring-app

WORKDIR /opt/build
COPY --from=maven-build /app/target/exchange-rates-app-0.0.1-SNAPSHOT.jar ./application.jar
COPY run.sh run.sh

RUN java -Djarmode=layertools -jar application.jar extract
RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress 2 \
    --output jdk

FROM debian:buster-slim

ARG APP_BUILD_PATH=/opt/build
ENV JAVA_HOME=/opt/jdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN groupadd --gid 1000 spring-app \
    && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app
USER spring-app:spring-app

WORKDIR /opt/workspace
COPY --from=app-build $APP_BUILD_PATH/jdk $JAVA_HOME
RUN true
COPY --from=app-build $APP_BUILD_PATH/spring-boot-loader/ ./
RUN true
COPY --from=app-build $APP_BUILD_PATH/dependencies/ ./
RUN true
COPY --from=app-build $APP_BUILD_PATH/snapshot-dependencies/ ./
RUN true
COPY --from=app-build $APP_BUILD_PATH/application/ ./
RUN true
COPY --from=app-build $APP_BUILD_PATH/run.sh ./

CMD ["/bin/sh", "run.sh"]