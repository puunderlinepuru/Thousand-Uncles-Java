FROM eclipse-temurin:21
LABEL authors="popo"

COPY ./build/tracker-0.0.1-SNAPSHOT.jar ./app.jar
COPY resources/ /resources/

EXPOSE 8084

ENTRYPOINT "java" "-jar" "app.jar"
