# Step 1: Build using Maven 3.9 and Java 17 (Matching your 3.9.12)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# We use the Maven wrapper if present, or just mvn
RUN mvn clean package -DskipTests

# Step 2: Run using Java 17 Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# This grabs the JAR built in the previous step
COPY --from=build /app/target/*.jar ananta-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ananta-api.jar"]