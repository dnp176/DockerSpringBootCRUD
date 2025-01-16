# Use a lightweight base image
FROM openjdk:17-jdk-slim

# Add a maintainer (optional, good practice)
#LABEL maintainer="yourname@example.com"

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/DockerSpringBootCRUD.jar app.jar

# Expose the port your application runs on
EXPOSE 9001

# Set environment variables
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_NAME=testdb_docker
ENV DB_USERNAME=root
ENV DB_PASSWORD=password

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
