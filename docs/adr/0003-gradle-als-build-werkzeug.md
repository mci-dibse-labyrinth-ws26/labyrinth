# 0003. Gradle als Build-Werkzeug

- Status: angenommen
- Datum: 30.09.2026 (Umsetzungsdetails 08.10.2026)
- Entschieden von: Gruppe (Werkzeug), Technikverantwortung (Umsetzung)

## Kontext

Ein Build-Werkzeug übersetzt den Code, lädt Bibliotheken, führt Tests aus und
paketiert die Programme. Für Java kommen praktisch Maven und Gradle in Frage.
Anforderungen aus [ADR 0002](0002-ein-repository-mit-modulen.md) und dem
Lastenheft:

- mehrere Module mit erzwungenen Abhängigkeitsregeln
- JavaFX, das seit Java 11 nicht mehr im JDK enthalten ist
- gemischtes Team mit Windows und macOS
- niemand soll Werkzeuge in bestimmten Versionen installieren müssen

## Entscheidung

**Gradle**, aufgerufen ausschließlich über den **Gradle Wrapper** (`gradlew`).

Umsetzung (08.10.2026):

- Gradle 9.8.0, Build-Skripte in **Kotlin-DSL** (`*.gradle.kts`)
- gemeinsame Einstellungen aller Java-Module in einem **Konventions-Plugin**
  `labyrinth.java-conventions` unter `buildSrc/`
- alle Versionen von Bibliotheken und Plugins in einem **Versionskatalog**
  `gradle/libs.versions.toml`
- Bibliotheken nur aus Maven Central, zentral in `settings.gradle.kts`
  festgelegt

Details zum Aufbau: [`devops.md`, Abschnitt 4](../devops.md#4-build).

## Begründung

- **Mehrere Module** sind in Gradle ein Kernkonzept; eine Abhängigkeit ist eine
  Zeile. In Maven braucht es ein Eltern-POM und ein POM pro Modul.
- **JavaFX** lässt sich über ein Plugin passend zum Betriebssystem einbinden.
- **Wrapper:** Die Gradle-Version liegt im Repository. Niemand installiert
  Gradle, und alle bauen mit derselben Version, lokal wie in der Pipeline.
- **Kotlin-DSL** ist seit Gradle 8.2 der Standard und wird von IntelliJ mit
  Autovervollständigung und Fehlermarkierung unterstützt.
- **Konventions-Plugin statt Konfiguration im Wurzelprojekt
  (`subprojects { }`)**: von Gradle empfohlen. Jedes Modul sagt sichtbar, was
  es ist, statt von außen unsichtbar konfiguriert zu werden.
- **Versionskatalog:** Jede Version steht genau einmal. Eine Aktualisierung ist
  eine Zeile, und alle Module ziehen mit.

## Betrachtete Alternativen

- **Maven:** sehr berechenbar und verbreitet, aber Mehrmodul-Projekte und
  JavaFX erfordern mehr Handarbeit, und Abweichungen vom Standardablauf sind
  in XML umständlich. Wäre vertretbar gewesen; entscheidend ist, nicht zu
  mischen.
- **Groovy-DSL:** ältere Schreibweise für Gradle-Skripte, schwächere
  IDE-Unterstützung.
- **Gemeinsame Konfiguration in `subprojects { }`:** einfacher hinzuschreiben,
  aber von Gradle nicht mehr empfohlen und für Leser eines Moduls unsichtbar.

## Konsequenzen

- Steilere Lernkurve als Maven. Abgefedert durch die Anleitung
  [`entwicklungsumgebung.md`](../entwicklungsumgebung.md) und kurze, gleich
  aufgebaute Build-Dateien pro Modul.
- Neue Bibliotheken werden zuerst im Versionskatalog eingetragen, dann im
  Modul benutzt.
- Das JavaFX-Plugin (0.1.0) wird nicht mehr aktiv gepflegt und erzeugt eine
  Warnung zur Kompatibilität mit Gradle 10. Vor einem Wechsel auf Gradle 10 ist
  das zu prüfen (siehe `devops.md`, Abschnitt 4.11).
