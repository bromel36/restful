# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle.kts, settings.gradle.kts, and gradle wrapper files
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle ./gradle

# Copy the source code
COPY src ./src

# Run the Gradle wrapper to build the application
RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the application
CMD ["java", "-jar", "build/libs/jobhunter-0.0.1-SNAPSHOT.jar"]
