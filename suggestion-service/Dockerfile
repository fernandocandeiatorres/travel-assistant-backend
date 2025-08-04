# --- Estágio 1: Build (O Construtor) ---
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# --- Estágio 2: Run (A Imagem Final) ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta definida para este serviço.
EXPOSE 8010

# Comando para executar a aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]