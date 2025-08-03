FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
# COPY --from=build /app/target/*-runner.jar app.jar
COPY . .
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]
# CMD [ "bash" ]