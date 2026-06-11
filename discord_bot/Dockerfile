FROM eclipse-temurin:21

COPY target/discord_bot-0.0.1-SNAPSHOT.jar ./app.jar
COPY resources/ /resources/

EXPOSE 8081

ENTRYPOINT "java" "-DBOT_TOKEN=$ENV" "-jar" "app.jar"