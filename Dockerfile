# Etapa de Execução com Eclipse Temurin JRE 17
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o artefato compilado pelo Maven
COPY target/*.jar app.jar

# Expõe a porta padrão da aplicação Spring Boot
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
