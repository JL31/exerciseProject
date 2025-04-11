# Step 1 : Maven build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy mandatory files for build purpose
COPY pom.xml .
COPY src ./src

# Project build (JAR packaging)
RUN mvn clean package -DskipTests

# Step 2 : Light image for execution purpose
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy JAR file from previous step
COPY --from=build /app/target/*.jar app.jar

# Application port exposure
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
