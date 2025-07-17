FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl

COPY target/main-app.jar app.jar
COPY wait-for-eureka.sh wait-for-eureka.sh

RUN chmod +x wait-for-eureka.sh

ENTRYPOINT ["./wait-for-eureka.sh"]
