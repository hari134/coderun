# Use an official OpenJDK runtime as the base image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at the defined working directory
COPY target/coderun-1.0-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Specify the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
