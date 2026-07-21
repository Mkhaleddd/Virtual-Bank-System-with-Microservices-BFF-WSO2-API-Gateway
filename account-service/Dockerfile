FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src/
RUN ./mvnw clean package -DskipTests
EXPOSE 8082
CMD ["java", "-jar", "target/*.jar"]
