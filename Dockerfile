# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","target/authy-app.jar"]
