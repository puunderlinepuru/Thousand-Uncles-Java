FROM eclipse-temurin:21
LABEL authors="popo"

COPY target/google_api_handler-0.0.1-SNAPSHOT.jar ./app.jar

COPY shared/records.json /shared/records.json
COPY ./google_token/StoredCredential /tokens/StoredCredential

EXPOSE 8082

ENTRYPOINT "java" "-jar" "app.jar"