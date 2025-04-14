# Etapa 1: Build con Maven y JDK 17
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

# Crea el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el contenido del proyecto al contenedor
COPY . .

# Compila el proyecto y genera el .jar (sin ejecutar tests)
RUN mvn clean install -DskipTests

# Etapa 2: Imagen final más ligera para ejecutar la app
FROM eclipse-temurin:17-jdk-alpine

# Directorio donde se ejecutará el .jar
WORKDIR /app

# Copia el .jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto estándar de Spring Boot
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]