# --- Estágio 1: Build (O Construtor) ---
# Usamos uma imagem do Maven com Java 21 para compilar o projeto.
# Damos a este estágio o nome "builder".
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache do Docker.
# Se as dependências não mudarem, o Docker reutiliza esta camada.
COPY pom.xml .

# Baixa todas as dependências do projeto.
RUN mvn dependency:go-offline

# Copia todo o resto do código-fonte.
COPY src ./src

# Compila o projeto e o empacota em um arquivo .jar, pulando os testes.
RUN mvn clean package -DskipTests


# --- Estágio 2: Run (A Imagem Final) ---
# Usamos uma imagem leve, contendo apenas o Java Runtime, para a imagem final.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia APENAS o arquivo .jar gerado no estágio "builder".
# Isso resulta em uma imagem muito menor e mais segura.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta em que a aplicação Spring Boot irá rodar.
EXPOSE 8000

# Comando para executar a aplicação quando o contêiner iniciar.
ENTRYPOINT ["java", "-jar", "app.jar"]