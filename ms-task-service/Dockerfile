# Usa una imagen base de Maven para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos necesarios para compilar
COPY pom.xml .
COPY src ./src

# Compila el proyecto y genera el JAR (sin ejecutar tests)
RUN mvn clean package -DskipTests

# Usa una imagen más liviana para ejecutar el JAR
FROM eclipse-temurin:17-jdk-alpine

# Crea un directorio para la app
WORKDIR /app

# Copia el JAR generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto que tu app usa (debe coincidir con server.port)
EXPOSE 8046

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]