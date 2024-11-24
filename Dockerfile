# Use a lightweight Alpine base image with OpenJDK 17
FROM openjdk:17-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/product-service-0.0.1-SNAPSHOT.jar /app/product-service.jar

# Expose the application port
EXPOSE 8082

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]
