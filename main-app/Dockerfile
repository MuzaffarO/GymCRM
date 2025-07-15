FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/main-app.jar app.jar

ENV REDIS_ENABLED=false
ENV ACTIVEMQ_ENABLED=false
ENV POSTGRES_ENABLED=false
ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
