# 0007. Build und Tests mit GitHub Actions automatisieren

- Status: angenommen
- Datum: 26.10.2026
- Entschieden von: Technikverantwortung

## Kontext

Sieben Personen arbeiten auf Windows und macOS am selben Repository. Ob eine
Änderung baut und alle Tests besteht, hing bisher allein davon ab, dass jede
Person vor dem Commit lokal baut. Ein vergessener Build oder etwas, das nur
auf einem Rechner installiert ist, fällt so erst auf, wenn andere schon darauf
aufbauen.

Für den geplanten Branch-Schutz (`devops.md`, Abschnitt 6) braucht `main`
außerdem eine automatische Prüfung, die vor dem Merge grün sein muss.

## Entscheidung

- **GitHub Actions** baut und testet das Projekt mit `./gradlew build`
  (`.github/workflows/build.yml`).
- Auslöser: jeder **Pull Request**, jeder **Push auf `main`**, zusätzlich von
  Hand. Branches ohne Pull Request werden nicht gebaut.
- Ein Job `build` auf **Linux** (`ubuntu-latest`) mit **Java 21 (Temurin)**.
- Gradle über `gradle/actions/setup-gradle` mit dem quelloffenen Cache
  (`cache-provider: basic`) und automatischer Prüfung des Gradle Wrappers.
- Das Zugangs-Token der Pipeline darf das Repository nur lesen.

## Begründung

- GitHub Actions ist in GitHub eingebaut, für öffentliche Repositories
  kostenlos und braucht keinen eigenen Server. Das Ergebnis steht direkt im
  Pull Request.
- Derselbe Befehl wie lokal und dieselbe Java-Version wie in der Toolchain:
  Was in der Pipeline scheitert, lässt sich lokal nachstellen.
- Linux ergänzt Windows und macOS im Team. Das Projekt baut damit regelmäßig
  auf allen drei Systemen, ohne jeden Lauf dreifach auszuführen.
- Pull Requests plus `main` decken alles ab, was in `main` landen kann, und
  jede Änderung wird genau einmal gebaut.
- Der Basic-Cache steht unter MIT-Lizenz. Die Standardvariante von
  `setup-gradle` („enhanced“) ist seit Version 6 eine proprietäre Komponente
  mit eigenen Nutzungsbedingungen; ihr Geschwindigkeitsvorteil ist bei einem
  Projekt dieser Größe gering.
- Die Wrapper-Prüfung verhindert, dass eine manipulierte `gradle-wrapper.jar`
  unbemerkt bei allen ausgeführt wird.

## Betrachtete Alternativen

- **Keine Pipeline, nur die Regel „lokal bauen“:** hängt an Disziplin, und
  ohne automatische Prüfung kann der Branch-Schutz kein grünes Ergebnis
  verlangen.
- **Jenkins oder ein anderer eigener Server:** müsste betrieben und gewartet
  werden.
- **Jeder Push auf jeden Branch:** Rückmeldung auch ohne Pull Request, aber
  Branches mit offenem Pull Request würden doppelt gebaut. Frühe Rückmeldung
  gibt stattdessen ein Pull Request als Entwurf.
- **Alle drei Betriebssysteme in der Pipeline (Matrix):** dreifache Laufzeit;
  Windows und macOS sind im Team ohnehin abgedeckt.
- **Cache über `actions/setup-java` (`cache: gradle`):** gleiche
  Cache-Strategie wie der Basic-Cache, aber ohne Wrapper-Prüfung und ohne
  Zusammenfassung des Laufs.
- **Actions auf einen festen Commit (SHA) festlegen statt auf die
  Hauptversion:** schützt noch besser vor veränderten Actions, macht aber jede
  Aktualisierung zur Handarbeit. Die eingesetzten Actions stammen von GitHub
  und Gradle selbst.

## Konsequenzen

- Jeder Pull Request zeigt ✓ oder ✗. Ein rotes Ergebnis wird vor dem Merge
  behoben.
- Der Job-Name `build` wird die Pflicht-Prüfung im Branch-Schutz und bleibt
  deshalb fest.
- Neue Hauptversionen der Actions werden von Hand eingetragen.
- Weitere Schritte (Testberichte, Testabdeckung, Releases) kommen in diesen
  Workflow oder in neue Dateien unter `.github/workflows/`.
