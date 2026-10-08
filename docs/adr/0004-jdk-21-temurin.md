# 0004. JDK 21 (Eclipse Temurin) über die Gradle-Toolchain

- Status: angenommen
- Datum: 30.09.2026 (Umsetzungsdetails 08.10.2026)
- Entschieden von: Gruppe (Version), Technikverantwortung (Umsetzung)

## Kontext

Unterschiedliche Java-Versionen im Team führen zu schwer zuzuordnenden Fehlern,
etwa `unsupported class file version`, wenn Code mit einer neueren Version
übersetzt und mit einer älteren ausgeführt wird. Die Teammitglieder haben
unterschiedliche oder gar keine JDKs installiert, teils noch sehr alte
(Java 8).

## Entscheidung

- Verbindliche Sprachversion: **Java 21** (Long-Term-Support).
- Empfohlene Distribution: **Eclipse Temurin**.
- Durchgesetzt über die **Gradle-Toolchain** im Konventions-Plugin: Gradle
  kompiliert, testet und startet immer mit Java 21, egal mit welchem Java
  Gradle selbst gestartet wurde.
- Fehlt ein passendes JDK, lädt Gradle es über das Plugin
  `foojay-resolver-convention` automatisch herunter.
- Folgeentscheidung (08.10.2026): **JavaFX 21**, weil JavaFX 25 und neuer ein
  neueres JDK voraussetzen.

## Begründung

- Entscheidend ist weniger *welche* Version, sondern dass **alle dieselbe**
  verwenden. Die Toolchain erzwingt das technisch, statt sich auf Absprachen
  zu verlassen.
- Java 21 wird langfristig gepflegt und von allen IDEs und CI-Systemen
  unterstützt.
- Temurin ist frei, ohne Lizenzeinschränkungen und weit verbreitet.
- Durch den automatischen Download muss niemand ein bestimmtes JDK von Hand
  installieren.

## Betrachtete Alternativen

- **Java 17:** älter, ebenfalls LTS, aber ohne die Sprachneuerungen von 21
  (z. B. Pattern Matching für `switch`, Record Patterns).
- **Java 25:** die neueste LTS-Version (seit September 2025) und technisch
  ebenso möglich. Die Gruppe hat sich für die länger erprobte und in Lehre und
  Werkzeugen am weitesten verbreitete Version 21 entschieden; Neuerungen aus
  25 werden für das Projekt nicht benötigt.
- **Nur Absprache „alle installieren Java 21“:** fehleranfällig; ein falsch
  gesetztes `JAVA_HOME` genügt.

## Konsequenzen

- Gradle selbst braucht zum Starten ein JDK ab Version 17. In IntelliJ wird es
  beim ersten Laden auf Nachfrage heruntergeladen; die Anleitung empfiehlt dort
  ebenfalls Temurin 21, damit nur ein JDK auf dem Rechner liegt.
- Wer außerhalb von IntelliJ im Terminal baut, muss `JAVA_HOME` setzen oder ein
  JDK installieren (siehe
  [`entwicklungsumgebung.md`, Abschnitt 6](../entwicklungsumgebung.md#6-kommandozeile-außerhalb-von-intellij)).
- Ein späterer Wechsel der Java-Version ist eine Zeile im Konventions-Plugin,
  braucht aber eine neue ADR.
