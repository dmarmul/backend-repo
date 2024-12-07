#Builder stage
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR application
COPY . .
RUN mvn clean package -DskipTests

#Final stage
FROM openjdk:17-jdk-slim
WORKDIR /application
COPY --from=builder /application/target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
