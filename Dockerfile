# Build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Deploy
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/whatsappintg-0.0.1-SNAPSHOT.jar /app/whatsappintg.jar
EXPOSE 5300
CMD ["java", "-jar", "whatsappintg.jar"]