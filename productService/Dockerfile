# Stage 1: Build the application
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/BMS-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3132
ENTRYPOINT ["java", "-jar", "app.jar"]