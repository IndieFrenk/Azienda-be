# Azienda Backend

Un'applicazione Spring Boot multi-modulo per la gestione aziendale.

## Funzionalità
Sistema di autenticazione utenti
Gestione feedback
Configurazioni di sicurezza
Templates email per reset password e nuove risposte


## Configurazione
Le configurazioni sono centralizzate in application.properties nel modulo AppMain.

## Architettura

Il progetto è strutturato come un'applicazione Maven multi-modulo con i seguenti componenti:

### Moduli

- **AppMain** - Modulo principale contenente la classe `Application` e le configurazioni base
- **Authentication** - Gestione autenticazione e utenti
- **Configuration** - Configurazioni di sicurezza e sistema
- **Feedback** - Sistema di gestione feedback
- **AziendaSoft** - Logica di business principale

### Struttura
Azienda-be/ ├── AppMain/ # Modulo principale
├── Authentication/ # Autenticazione utenti 
├── Configuration/ # Configurazioni sistema 
├── Feedback/ # Gestione feedback 
├── AziendaSoft/ # Business logic 
└── src/test/ # Test del progetto

## Tecnologie

- **Java** - Linguaggio di programmazione
- **Spring Boot** - Framework principale
- **Maven** - Build tool e gestione dipendenze
- **Railway** - Piattaforma di deployment

## Build e Deploy

### Build locale
```bash
./mvnw clean package -DskipTests

## Esecuzione

java -jar AppMain/target/AppMain-0.0.1-SNAPSHOT.jar