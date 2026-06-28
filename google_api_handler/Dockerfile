FROM eclipse-temurin:21
LABEL authors="popo"

COPY ./build/google_api_handler-0.0.1-SNAPSHOT.jar ./app.jar
COPY ./tokens/StoredCredential /tokens/StoredCredential

EXPOSE 8082

ENTRYPOINT "java" "-jar" "app.jar"
