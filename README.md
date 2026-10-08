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
| `docs` | Pflichtenheft, Testplan, Anleitungen, Architekturentscheidungen |

## Loslegen

Voraussetzungen sind nur **Git** und **IntelliJ IDEA**. Gradle und das passende
JDK 21 holt sich das Projekt selbst. Die Schritt-für-Schritt-Anleitung steht in
[`docs/entwicklungsumgebung.md`](docs/entwicklungsumgebung.md).

## Build

    ./gradlew build

Unter Windows: `.\gradlew.bat build`

Weitere Befehle (Server und Client starten, nur Tests) stehen in der Anleitung.

## Dokumentation

| Dokument | Inhalt |
|---|---|
| [`docs/entwicklungsumgebung.md`](docs/entwicklungsumgebung.md) | Rechner einrichten, Projekt bauen und starten |
| [`CONTRIBUTING.md`](CONTRIBUTING.md) | Branches, Commits, Pull Requests, Reviews |
| [`docs/devops.md`](docs/devops.md) | Betriebshandbuch: Repository, Build, Pipeline, Releases |
| [`docs/adr/`](docs/adr/README.md) | Architekturentscheidungen mit Begründung |

## Hinweis

Dieses Projekt entsteht im Rahmen einer Lehrveranstaltung. "Das verrückte Labyrinth" ist ein Spiel von Ravensburger; Namensrechte und Originalgrafiken liegen beim Rechteinhaber. Es werden keine Originalassets verwendet.
