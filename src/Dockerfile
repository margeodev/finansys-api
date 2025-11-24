# Etapa 1: Build do projeto com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (Otimizada)
FROM eclipse-temurin:17-jre-focal # Imagem menor, apenas com JRE
WORKDIR /app

# Cria um usuário não-root para segurança (melhor prática)
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
