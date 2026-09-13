# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the POM first so dependency downloads are cached
# separately from source changes (faster rebuilds).
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the source and build the jar (skip tests during the
# container build — run `mvn test` locally/in CI instead).
COPY src ./src
RUN mvn -B clean package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Render sets PORT at runtime; Spring Boot reads server.port from it.
ENV PORT=8080
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
