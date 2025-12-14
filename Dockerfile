# -------- BUILD STAGE --------
FROM eclipse-temurin:24 AS build
WORKDIR /app

COPY pom.xml .
RUN ./mvnw dependency:go-offline || true

COPY src ./src
RUN ./mvnw clean package -DskipTests

# -------- RUNTIME STAGE --------
FROM eclipse-temurin:24-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
