FROM eclipse-temurin:25

COPY target/discord_bot-0.0.1-SNAPSHOT.jar ./app.jar

RUN ls -l ./

ENTRYPOINT "java" "-DBOT_TOKEN=$ENV" "-jar" "app.jar"