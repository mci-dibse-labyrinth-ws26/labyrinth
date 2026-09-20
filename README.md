# Das verrückte Labyrinth

Verteilte Umsetzung des Spiels "Das verrückte Labyrinth" als Client-Server-Anwendung.

Advanced Integrative Project, Master Digital Business & Software Engineering, MCI Innsbruck, WS 2026.

## Aufbau

| Modul | Inhalt |
|---|---|
| `protocol` | Nachrichtenklassen und JSON-Serialisierung |
| `core` | Domänenmodell und Spielregeln |
| `server` | Spielserver |
| `directory-server` | Verzeichnisserver |
| `client-desktop` | Desktop-Client (JavaFX) |
| `docs` | Pflichtenheft, Testplan, Protokolle |

## Voraussetzungen

- JDK 21
- Git

## Build

    ./gradlew build

Unter Windows: `gradlew.bat build`

## Hinweis

Dieses Projekt entsteht im Rahmen einer Lehrveranstaltung. "Das verrückte Labyrinth" ist ein Spiel von Ravensburger; Namensrechte und Originalgrafiken liegen beim Rechteinhaber. Es werden keine Originalassets verwendet.
