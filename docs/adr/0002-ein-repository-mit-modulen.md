# 0002. Ein Repository mit Modulen

- Status: angenommen
- Datum: 30.09.2026
- Entschieden von: Gruppe (Vorschlag der Technikverantwortung)

## Kontext

Das System besteht aus Spielserver, Verzeichnisserver und mindestens einem
Java-Desktop-Client mit KI. Server und Client teilen zwangsläufig zwei Dinge:

- die **Nachrichtenklassen** des Protokolls. Jede Änderung muss auf beiden
  Seiten gleichzeitig ankommen.
- die **Spielregeln**. Der Server prüft damit Züge, die KI im Client simuliert
  damit Züge. Zweimal umgesetzt laufen die Varianten auseinander, und die KI
  schlägt Züge vor, die der Server ablehnt.

In der Anfangsphase ändert sich das Protokoll häufig.

## Entscheidung

Ein gemeinsames Repository mit fünf Modulen:

| Modul | Inhalt | benutzt |
|---|---|---|
| `protocol` | Nachrichtenklassen, JSON-Serialisierung | – |
| `core` | Spielmodell, Spielregeln | – |
| `server` | Spielserver | `protocol`, `core` |
| `directory-server` | Verzeichnisserver | `protocol` |
| `client-desktop` | JavaFX-Client mit KI | `protocol`, `core` |

Die erlaubten Abhängigkeiten setzt das Build-Werkzeug durch (siehe
[ADR 0003](0003-gradle-als-build-werkzeug.md)). Zuständigkeiten für einzelne
Ordner werden über `.github/CODEOWNERS` geregelt.

Bei der Umsetzung festgelegt (08.10.2026): `core` und `protocol` hängen nicht
voneinander ab, weil das Protokoll gruppenübergreifend einheitlich sein muss
(Lastenheft, Abschnitt 5) und nicht an unseren internen Klassen hängen soll.

## Begründung

- Eine Protokolländerung ist **ein** Pull Request statt vier (Protokoll-Repo,
  Version veröffentlichen, Server-Repo, Client-Repo).
- Keine Paketregistry nötig, um gemeinsamen Code zu verteilen.
- Ein Build und eine Pipeline prüfen das Zusammenspiel aller Teile bei jeder
  Änderung.
- Modulgrenzen und `CODEOWNERS` trennen Zuständigkeiten genauso verbindlich
  wie getrennte Repositories.

## Betrachtete Alternativen

- **Getrennte Repositories für Server, Client und Protokoll:** Jede
  Protokolländerung braucht mehrere abgestimmte Pull Requests und eine
  veröffentlichte Bibliotheksversion. Zu viel Reibung in einer Phase mit
  häufigen Änderungen.
- **Ein Repository ohne Module:** Keine technischen Grenzen. Client-Code könnte
  unbemerkt Server-Klassen benutzen.

## Konsequenzen

- Das Repository ist größer als jedes einzelne getrennte wäre.
- Zugriffsrechte lassen sich nicht pro Modul vergeben, nur Review-Pflichten
  über `CODEOWNERS`.
- Optionale Clients in anderer Technologie (Browser, Smartphone) bekommen
  bei Bedarf ein eigenes Repository mit eigenem Build.
- Rückweg möglich: `git subtree split` löst ein Modul samt Historie in ein
  eigenes Repository heraus.
