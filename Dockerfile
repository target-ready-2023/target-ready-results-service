#
# Build stage
#
FROM gradle:jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

#
# Package stage
#
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/ /app/
EXPOSE 8080
CMD ["java","-jar","/app/TargetReadyResultsService-0.0.1.jar"]