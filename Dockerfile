FROM gradle:7.4-jdk17 AS builder

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-build-cache

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/TargetReadyResultsService-0.0.1.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]