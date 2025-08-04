FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl

COPY target/trainer-workload-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
