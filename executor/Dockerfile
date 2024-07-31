FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at the defined working directory
COPY target/executor-1.0-SNAPSHOT.jar app.jar

# Specify the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]