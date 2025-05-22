# Use Eclipse Temurin Java 21 image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (for better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Pre-download dependencies to leverage caching
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the project
COPY src ./src

# Build the application (skip tests for speed)
RUN ./mvnw clean install -DskipTests

# Expose the Spring Boot default port
EXPOSE 8080

# Run the built JAR
CMD ["java", "-jar", "target/recipe-0.0.1-SNAPSHOT.jar"]
