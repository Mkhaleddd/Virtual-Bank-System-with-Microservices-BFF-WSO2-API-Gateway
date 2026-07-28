FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src/
RUN ./mvnw clean package -DskipTests
EXPOSE 8084

# Use shell form so the wildcard (*) expands correctly, or target the exact jar file name
ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]