# Use Eclipse Temurin Java 21 image
FROM eclipse-temurin:21-jdk

# Create app directory
WORKDIR /app

# Copy everything
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build the app (skip tests for faster deploy)
RUN ./mvnw clean install -DskipTests

# Run the JAR
CMD ["java", "-jar", "target/recipe-0.0.1-SNAPSHOT.jar"]
