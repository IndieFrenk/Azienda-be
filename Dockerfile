FROM maven:3.9.4-openjdk-22-slim AS build

WORKDIR /app

# Copia i file pom per il caching delle dipendenze
COPY pom.xml .
COPY AppMain/pom.xml AppMain/
COPY Authentication/pom.xml Authentication/
COPY Configuration/pom.xml Configuration/
COPY Feedback/pom.xml Feedback/

# Scarica le dipendenze
RUN mvn dependency:go-offline -B

# Copia il codice sorgente
COPY . .

# Compila il progetto
RUN mvn clean package -DskipTests -pl AppMain -am

# Stage finale per l'esecuzione
FROM openjdk:22-jdk-slim

WORKDIR /app

# Copia il JAR eseguibile dal build stage
COPY --from=build /app/AppMain/target/AppMain-*.jar app.jar

EXPOSE 8080

# Usa java -jar con il JAR eseguibile
CMD ["java", "-jar", "app.jar"]