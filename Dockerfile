FROM openjdk:11-jdk as app-build
ENV RELEASE=11

RUN groupadd --gid 1000 spring-app \
    && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app

USER spring-app:spring-app
WORKDIR /opt/build

COPY ./target/exchange-rates-app-0.0.1-SNAPSHOT.jar ./application.jar
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

ARG BUILD_PATH=/opt/build
ENV JAVA_HOME=/opt/jdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN groupadd --gid 1000 spring-app \
    && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app

USER spring-app:spring-app
WORKDIR /opt/workspace

COPY --from=app-build $BUILD_PATH/jdk $JAVA_HOME
RUN true
COPY --from=app-build $BUILD_PATH/spring-boot-loader/ ./
RUN true
COPY --from=app-build $BUILD_PATH/dependencies/ ./
RUN true
COPY --from=app-build $BUILD_PATH/snapshot-dependencies/ ./
RUN true
COPY --from=app-build $BUILD_PATH/application/ ./
RUN true
COPY --from=app-build $BUILD_PATH/run.sh ./

CMD ["/bin/sh", "run.sh"]