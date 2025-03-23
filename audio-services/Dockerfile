FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Copy the JAR file and dependencies
COPY target/audio-service.jar /app/
COPY target/libs /app/libs

# Create directory for resources
RUN mkdir -p /app/resources

# Environment variables
ENV SERVER_PORT=8085

# Expose the service port
EXPOSE ${SERVER_PORT}

# Set the entry point
ENTRYPOINT ["java", "-jar", "audio-service.jar"]
