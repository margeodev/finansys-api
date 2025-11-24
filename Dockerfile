# Etapa 1: Build da aplicação
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR gerado da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
