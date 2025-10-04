# ===========================
# Stage 1: Build the project
# ===========================
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy only the necessary project files
COPY jobscraper/pom.xml ./jobscraper/
COPY jobscraper/.mvn ./jobscraper/.mvn
COPY jobscraper/src ./jobscraper/src

# Move into jobscraper folder
WORKDIR /app/jobscraper

# Make mvnw executable
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk:alpine

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/jobscraper/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]
