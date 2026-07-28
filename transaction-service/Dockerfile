FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src/
RUN ./mvnw clean package -DskipTests
EXPOSE 8083
RUN cp target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
