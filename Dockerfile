    FROM maven:3-openjdk-17-slim AS build
    WORKDIR /app
    COPY pom.xml ./
    RUN mvn dependency:go-offline
    COPY src ./src
    RUN mvn clean package -DskipTests
    RUN ls -la /app/target
    
    FROM openjdk:17-ea-17-jdk-slim
    WORKDIR /app
    COPY --from=build /app/target/NutriApp-0.0.1-SNAPSHOT.jar ./NutriApp.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "NutriApp.jar"]
