# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the entire project
COPY jobscraper/ ./jobscraper

# Move into jobscraper folder
WORKDIR /app/jobscraper

# Give permission to mvnw
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/jobscraper-0.0.1-SNAPSHOT.jar"]
