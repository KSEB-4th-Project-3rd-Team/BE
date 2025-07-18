# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Grant executable permissions to the Gradle wrapper
RUN chmod +x ./gradlew

# Copy the source code
COPY src ./src

# ✅ Copy the .env file into the image
COPY .env .env

# Build the application
RUN ./gradlew bootJar

# Expose the port the app runs on
EXPOSE 8080

# ✅ Specify the command to run on container startup
# export .env variables at runtime, then run the Spring Boot jar
CMD ["bash", "-c", "export $(cat .env | xargs) && java -jar build/libs/Smart_WMS_BE-0.0.1-SNAPSHOT.jar"]
