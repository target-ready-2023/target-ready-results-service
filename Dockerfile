FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/ /app/
EXPOSE 8080
CMD ["java","-jar","/app/TargetReadyResultsService-0.0.1.jar"]