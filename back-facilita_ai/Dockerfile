FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
RUN apk add --no-cache bash sed

COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
RUN sed -i 's/\r$//' mvnw && chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]