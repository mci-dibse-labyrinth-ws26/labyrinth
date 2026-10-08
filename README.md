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

## Schnellstart

Du brauchst nur **Git** und **IntelliJ IDEA**. Gradle und Java 21 holt sich das
Projekt selbst.

1. In IntelliJ **Clone Repository** wählen und diese URL eintragen:
   `https://github.com/mci-dibse-labyrinth-ws26/labyrinth.git`
2. **Load Gradle Project** bestätigen. Fragt IntelliJ nach einem JDK, dort
   **Version 21, Eclipse Temurin** auswählen und herunterladen lassen.
3. Im Gradle-Fenster rechts `labyrinth → Tasks → build → build` ausführen.
   Ergebnis: **BUILD SUCCESSFUL**.

Die ausführliche Anleitung mit Git-Einrichtung, macOS-Hinweisen und Lösungen
für häufige Probleme steht in
[`docs/entwicklungsumgebung.md`](docs/entwicklungsumgebung.md).

## Bauen und starten

Im IntelliJ-Terminal (unter macOS/Linux `./gradlew` statt `.\gradlew.bat`):

| Was | Befehl |
|---|---|
| Alles bauen und testen | `.\gradlew.bat build` |
| Nur die Tests | `.\gradlew.bat test` |
| Spielserver starten | `.\gradlew.bat :server:run` |
| Verzeichnisserver starten | `.\gradlew.bat :directory-server:run` |
| Desktop-Client starten | `.\gradlew.bat :client-desktop:run` |

## Mitarbeiten

Jede Änderung läuft über einen eigenen Branch und einen Pull Request mit
Review. Branch-Namen, Commit-Format und Ablauf stehen in
[`CONTRIBUTING.md`](CONTRIBUTING.md). Aufgaben verwalten wir in Jira, nicht in
GitHub.

## Dokumentation

| Dokument | Inhalt |
|---|---|
| [`docs/entwicklungsumgebung.md`](docs/entwicklungsumgebung.md) | Rechner einrichten, Projekt bauen und starten, Code richtig ablegen |
| [`CONTRIBUTING.md`](CONTRIBUTING.md) | Branches, Commits, Pull Requests, Reviews |
| [`docs/devops.md`](docs/devops.md) | Betriebshandbuch: Repository, Build, Pipeline, Releases |
| [`docs/adr/`](docs/adr/README.md) | Architekturentscheidungen mit Begründung |

## Hinweis

Dieses Projekt entsteht im Rahmen einer Lehrveranstaltung. "Das verrückte Labyrinth" ist ein Spiel von Ravensburger; Namensrechte und Originalgrafiken liegen beim Rechteinhaber. Es werden keine Originalassets verwendet.
