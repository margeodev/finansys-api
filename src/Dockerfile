# Etapa 1: Build do projeto com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta padrão do Spring Boot
EXPOSE 8080
# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
