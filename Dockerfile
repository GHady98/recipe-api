# Step 1: Build the app using Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the app with JDK
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/recipe-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
