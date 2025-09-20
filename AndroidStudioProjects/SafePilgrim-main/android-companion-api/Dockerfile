FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the built JAR file
COPY build/libs/android-companion-api-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/safepilgrim
ENV REDIS_URL=redis://redis:6379

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]
