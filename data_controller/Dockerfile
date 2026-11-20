FROM eclipse-temurin:25
LABEL authors="popo"

COPY ./build/data_controller-0.0.1-SNAPSHOT.jar ./app.jar
COPY resources/ /resources/

ENTRYPOINT exec "java" "-jar" "app.jar"
