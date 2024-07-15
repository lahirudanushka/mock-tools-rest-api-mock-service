# Stage 1: Build
FROM maven:3.8.7-jdk-17-alpine AS build
WORKDIR /usr/src/app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

# Stage 2: Run
FROM openjdk:17-jdk-slim-alpine

COPY --from=build /usr/src/app/target/*.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]

