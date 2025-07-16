FROM openjdk:21-jdk-slim as Build
WORKDIR /app
COPY target/GameServerUne-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080