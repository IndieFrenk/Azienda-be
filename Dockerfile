FROM openjdk:22-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY AppMain/pom.xml AppMain/
COPY Authentication/pom.xml Authentication/
COPY Configuration/pom.xml Configuration/
COPY Feedback/pom.xml Feedback/

COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "AppMain/target/AppMain-0.0.1-SNAPSHOT.jar"]