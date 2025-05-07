# Build stage
FROM gradle:7.4-jdk17 AS build
WORKDIR /app

# Copy tất cả các file vào container
COPY . .

# Chạy lệnh build với Gradle (thay vì Maven)
RUN gradle clean build -x test

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy file JAR đã được build từ build stage
COPY --from=build /app/build/libs/jobhunter-0.0.1-SNAPSHOT.jar jobhunter.jar

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "jobhunter.jar"]
