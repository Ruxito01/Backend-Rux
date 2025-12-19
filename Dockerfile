# Primera etapa: compilar
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permisos de ejecución a mvnw
RUN chmod +x mvnw

# Descargar dependencias (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar
RUN ./mvnw package -DskipTests

# Segunda etapa: imagen final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]