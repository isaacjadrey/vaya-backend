# --- Build stage -----------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

# Cache Gradle wrapper + downloaded dependencies
# RUN --mount=type=cache, target=/root/.gradle \ ./gradlew dependencies --no-daemon

# Now copy the rest and build
COPY src src

RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar --no-daemon -x test

# --- Run stage ---------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]