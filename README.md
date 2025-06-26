# Azienda Backend

This project is a multi-module Spring Boot application built with Maven. The codebase is split into four modules and a parent project.

## Modules

| Module | Description |
|-------|-------------|
| **AppMain** | Entry point of the application. Contains the `Application` class and application level resources. Depends on the other modules. |
| **Authentication** | Handles users, roles, JWT based login and password recovery. Provides REST controllers for registration and authentication. |
| **Configuration** | Contains security configuration such as JWT utilities and Spring security beans. |
| **Feedback** | Implements feedback management with entities, services and controllers for feedback, context and replies. |

The root `pom.xml` defines these modules and common dependencies.

## Build

Java 22 and Maven are required. You can use the provided Maven wrapper:

```bash
chmod +x mvnw        # first time only
./mvnw clean package
```

This builds all modules and produces JAR files under each module's `target` directory.

## Run

After building the project you can run the main application:

```bash
java -jar AppMain/target/AppMain-0.0.1-SNAPSHOT.jar
```

The server listens on port `7777` by default (see `AppMain/src/main/resources/application.properties`).

## Basic Usage

The application exposes REST endpoints for authentication and feedback management. Example controllers include `AuthController` under the Authentication module and `FeedbackController` under the Feedback module. Refer to the respective source files for available routes and request bodies.

