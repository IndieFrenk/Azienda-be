FROM maven:3.9.4-openjdk-22-slim AS build

WORKDIR /app

COPY pom.xml .
COPY AppMain/pom.xml AppMain/
COPY Authentication/pom.xml Authentication/
COPY Configuration/pom.xml Configuration/
COPY Feedback/pom.xml Feedback/

RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk-slim

WORKDIR /app

COPY --from=build /app/AppMain/target/AppMain-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]