FROM eclipse-temurin:21
LABEL authors="popo"

COPY target/google_api_handler-0.0.1-SNAPSHOT.jar ./app.jar

COPY resources/records.json /resources/records.json
COPY tokens/StoredCredential /tokens/StoredCredential

ENTRYPOINT "java" "-jar" "app.jar"