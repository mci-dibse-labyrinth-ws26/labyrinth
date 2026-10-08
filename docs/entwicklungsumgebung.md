# Entwicklungsumgebung einrichten

Diese Anleitung bringt dich vom leeren Rechner bis zum ersten erfolgreichen
Build. Rechne mit etwa 20 Minuten, davon ist das meiste Download-Zeit.

Sie wurde am 08.10.2026 auf einem frisch eingerichteten Windows-11-Laptop
Schritt für Schritt durchgespielt. Wenn bei dir etwas anders aussieht oder
hakt, sag Bescheid oder bessere die Anleitung per Pull Request nach.

## Inhalt

1. [Kurzfassung](#1-kurzfassung)
2. [Was du brauchst](#2-was-du-brauchst)
3. [Einmalige Einrichtung](#3-einmalige-einrichtung)
4. [Projekt holen](#4-projekt-holen)
5. [Bauen, testen, starten](#5-bauen-testen-starten)
6. [Kommandozeile außerhalb von IntelliJ](#6-kommandozeile-außerhalb-von-intellij)
7. [Wo kommt mein Code hin?](#7-wo-kommt-mein-code-hin)
8. [Die erste eigene Änderung](#8-die-erste-eigene-änderung)
9. [Häufige Probleme](#9-häufige-probleme)

## 1. Kurzfassung

1. Git und IntelliJ IDEA installieren, Git einrichten.
2. In IntelliJ das Repository klonen.
3. Wenn IntelliJ nach einem JDK fragt: **Version 21, Eclipse Temurin**
   herunterladen lassen.
4. Im Gradle-Fenster `build` ausführen. Fertig.

Java und Gradle musst du **nicht** von Hand installieren. Die richtige
Gradle-Version bringt das Projekt selbst mit, das passende Java lädt IntelliJ
auf Nachfrage herunter.

## 2. Was du brauchst

| Was | Wofür |
|---|---|
| **Git** | Versionsverwaltung |
| **IntelliJ IDEA** | Entwicklungsumgebung; die kostenlose Ausgabe reicht, Studierende bekommen die Vollversion kostenlos |
| **GitHub-Account** | Mitglied der Organisation `mci-dibse-labyrinth-ws26` (Einladung per Mail annehmen) |

Unterstützt sind **Windows, macOS und Linux**, jeweils direkt auf dem System.
WSL (Linux unter Windows) ist nicht nötig und wird nicht empfohlen: Java läuft
auf allen Systemen gleich, und der JavaFX-Client öffnet ein Fenster, was aus
WSL heraus nur über Umwege funktioniert.

Eine andere IDE (Eclipse, VS Code) geht auch, weil das Projekt komplett über
Gradle gebaut wird. Diese Anleitung beschreibt IntelliJ. Mit einer anderen IDE
brauchst du zusätzlich ein JDK 21, siehe [Abschnitt 6](#6-kommandozeile-außerhalb-von-intellij).

## 3. Einmalige Einrichtung

### 3.1 Git installieren

**Windows** (PowerShell):

```powershell
winget install --id Git.Git -e --source winget
```

Oder den Installer von https://git-scm.com herunterladen und alle Vorgaben
übernehmen.

**macOS**: Im Terminal `git --version` eingeben. Ist Git nicht da, bietet macOS
an, die Xcode Command Line Tools zu installieren; die enthalten Git.

Danach ein **neues** Terminal öffnen und prüfen:

```
git --version
```

### 3.2 Git einrichten

```
git config --global user.name "Vorname Nachname"
git config --global user.email "deine-github-mail@example.com"
```

Die E-Mail muss dieselbe sein wie bei deinem GitHub-Account, sonst ordnet
GitHub deine Commits nicht deinem Profil zu.

**Zeilenenden**, je nach System:

```
# Windows
git config --global core.autocrlf true

# macOS / Linux
git config --global core.autocrlf input
```

Windows und macOS speichern Zeilenenden unterschiedlich. Ohne diese
Einstellung sehen Pull Requests aus, als hätte man jede Zeile geändert.

Prüfen: `git config --global --list` zeigt alle drei Einstellungen.

### 3.3 IntelliJ IDEA installieren

Von https://www.jetbrains.com/idea/download/ herunterladen und installieren.
Beim Start nach einem JetBrains-Konto gefragt? Die kostenlosen Funktionen
reichen; mit Studierenden-Lizenz bekommst du die Vollversion.

**Noch kein JDK installieren**, auch wenn IntelliJ es jetzt schon anbietet.
Das passiert im nächsten Abschnitt gezielt in der richtigen Version.

## 4. Projekt holen

1. Auf dem Willkommensbildschirm **Clone Repository** (bzw. „Get from VCS“).
2. URL: `https://github.com/mci-dibse-labyrinth-ws26/labyrinth.git`
3. Zielordner wählen, z. B. `C:\Users\<du>\Projects_MCI\labyrinth`.
4. **Clone**, dann **Trust Project**.

IntelliJ erkennt das Gradle-Projekt und bietet **Load Gradle Project** an:
anklicken.

Jetzt erscheint der Dialog **„Download Java to run Gradle“**. Gradle ist selbst
ein Java-Programm und braucht ein JDK zum Starten. Stelle ein:

| Feld | Wert |
|---|---|
| Version | **21** |
| Vendor | **Eclipse Temurin** |
| Location | Vorschlag übernehmen |

Nicht die vorausgewählte neueste Version nehmen. Mit 21 von Temurin hast du
genau das JDK, mit dem das Projekt gebaut wird. Gradle findet es auch für den
eigentlichen Build und muss kein zweites JDK herunterladen.

Danach lädt IntelliJ beim ersten Mal einiges herunter (Gradle, Bibliotheken).
Das dauert ein paar Minuten. Fertig ist es, wenn rechts im **Gradle-Fenster**
(Elefanten-Symbol) diese Einträge stehen:

```
labyrinth
├── buildSrc
├── client-desktop
├── core
├── directory-server
├── protocol
└── server
```

## 5. Bauen, testen, starten

### Im Gradle-Fenster (empfohlen)

| Was | Doppelklick auf |
|---|---|
| Alles bauen und testen | `labyrinth → Tasks → build → build` |
| Spielserver starten | `server → Tasks → application → run` |
| Verzeichnisserver starten | `directory-server → Tasks → application → run` |
| Desktop-Client starten | `client-desktop → Tasks → application → run` |

Erwartet:

- `build` endet mit **BUILD SUCCESSFUL**.
- Der Server gibt `Labyrinth-Server (Protokoll 0.1.0, Spielbrett 7x7)` aus.
- Der Client öffnet ein kleines Fenster mit dem Titel „Labyrinth“.

### Im IntelliJ-Terminal

Unten **Terminal** öffnen (Windows: Alt+F12, macOS: ⌥F12). IntelliJ gibt dem
Terminal das JDK automatisch mit, die Befehle funktionieren also sofort.

| Was | Windows | macOS / Linux |
|---|---|---|
| Alles bauen und testen | `.\gradlew.bat build` | `./gradlew build` |
| Nur die Tests | `.\gradlew.bat test` | `./gradlew test` |
| Spielserver starten | `.\gradlew.bat :server:run` | `./gradlew :server:run` |
| Client starten | `.\gradlew.bat :client-desktop:run` | `./gradlew :client-desktop:run` |

`gradlew` ist der **Gradle Wrapper**: ein kleines Skript im Repository, das die
im Projekt festgelegte Gradle-Version verwendet und beim ersten Aufruf
herunterlädt. Deshalb musst du Gradle nicht installieren, und alle bauen mit
derselben Version. Der Doppelpunkt in `:server:run` heißt: „im Modul `server`
die Aufgabe `run`“.

## 6. Kommandozeile außerhalb von IntelliJ

In einem normalen Terminal (PowerShell, macOS-Terminal, VS Code) kennt das
System das von IntelliJ heruntergeladene JDK nicht. `gradlew` meldet dann:

```
ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
```

Brauchst du das Terminal nur in IntelliJ, kannst du diesen Abschnitt
überspringen. Sonst gibt es zwei Wege.

**Weg A: das JDK von IntelliJ bekannt machen**

Windows (PowerShell; den Ordnernamen siehst du unter
`C:\Users\<du>\.jdks\`):

```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "$env:USERPROFILE\.jdks\temurin-21.0.12.1", "User")
```

Danach ein **neues** Terminal öffnen. `JAVA_HOME` ist die Umgebungsvariable,
über die `gradlew` und viele andere Werkzeuge Java finden.

macOS (in `~/.zshrc` eintragen, dann neues Terminal):

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

**Weg B: JDK 21 separat installieren**

```powershell
# Windows
winget install --id EclipseAdoptium.Temurin.21.JDK -e --source winget
```

```sh
# macOS (mit Homebrew)
brew install --cask temurin@21
```

Weg A ist unter Windows getestet. Die macOS-Befehle sind noch nicht im Team
durchgespielt; bitte beim ersten Mal bestätigen oder hier korrigieren.

Prüfen in jedem Fall mit einem Build im neuen Terminal:
`.\gradlew.bat build` bzw. `./gradlew build`.

## 7. Wo kommt mein Code hin?

Das Projekt besteht aus fünf **Modulen**. Jedes ist ein Unterordner mit eigener
Aufgabe:

| Modul | Inhalt | darf benutzen |
|---|---|---|
| `protocol` | Nachrichten zwischen Client und Server und ihre JSON-Umwandlung | – |
| `core` | Spielmodell und Spielregeln | – |
| `server` | Spielserver | `protocol`, `core` |
| `directory-server` | Verzeichnisserver (Liste der laufenden Spielserver) | `protocol` |
| `client-desktop` | JavaFX-Oberfläche und KI | `protocol`, `core` |

Die rechte Spalte erzwingt der Build: Was dort nicht steht, ist für das Modul
unsichtbar und führt zu einem Kompilierfehler. Das schränkt nicht ein, **wer**
woran arbeitet, sondern welcher **Code** welchen anderen benutzen darf. Braucht
ein Modul eine neue Verbindung, ist das eine Zeile in seiner
`build.gradle.kts` und wird wie jede Änderung per Pull Request besprochen.

Innerhalb eines Moduls:

```
server/
├── build.gradle.kts                          Build-Einstellungen des Moduls
└── src/
    ├── main/java/edu/mci/labyrinth/server/   Programmcode
    └── test/java/edu/mci/labyrinth/server/   Tests (JUnit)
```

Alle Pakete beginnen mit `edu.mci.labyrinth`, gefolgt vom Modulnamen
(`edu.mci.labyrinth.core`, `edu.mci.labyrinth.client` …). Tests liegen im
gleichen Paket wie die getestete Klasse, nur unter `src/test`.

Die vorhandenen Klassen (`ProtocolVersion`, `Board`, `ServerApp` …) sind
**Platzhalter** aus dem Projektgerüst und dürfen ersetzt werden.

## 8. Die erste eigene Änderung

Branch anlegen, committen, Pull Request: Das steht ausführlich in
[`CONTRIBUTING.md`](../CONTRIBUTING.md).

Vor dem ersten Push in IntelliJ einmal den GitHub-Account verbinden:
**Settings → Version Control → GitHub → +** und anmelden.

## 9. Häufige Probleme

**`JAVA_HOME is not set and no 'java' command could be found`**
Du baust in einem Terminal außerhalb von IntelliJ. Siehe
[Abschnitt 6](#6-kommandozeile-außerhalb-von-intellij).

**`JAVA_HOME is set to an invalid directory`**
`JAVA_HOME` zeigt auf einen Ordner, den es nicht gibt (Tippfehler, altes JDK
deinstalliert). Mit Weg A aus Abschnitt 6 neu setzen, neues Terminal öffnen.

**Gradle startet nicht, Meldung über eine zu alte Java-Version**
`JAVA_HOME` zeigt auf ein altes Java (z. B. 8). Gradle braucht zum Starten
mindestens Java 17. `JAVA_HOME` auf das JDK 21 umstellen (Abschnitt 6).

**IntelliJ zeigt „Code insight unavailable“ oder „Load Gradle Project“**
Das Gradle-Projekt ist noch nicht geladen. Auf **Load Gradle Project** klicken
oder im Gradle-Fenster auf das Aktualisieren-Symbol.

**„Some of the ignored directories are not excluded from indexing“**
Harmlos. **View directories** und alle ausschließen; betrifft nur deine lokale
IntelliJ-Suche.

**Erster Push aus IntelliJ: `403` / „Permission denied“**
Die Organisation lässt fremde Apps erst nach Freigabe zu. IntelliJ schickt
dabei automatisch eine Anfrage; Bescheid geben, die Technikverantwortung gibt
sie frei. Danach erneut pushen.

**Pull Request zeigt hunderte geänderte Zeilen**
Zeilenenden. `git config --global core.autocrlf` prüfen (Abschnitt 3.2).

**macOS: `./gradlew: Permission denied`**
Sollte nicht vorkommen, weil `gradlew` im Repository als ausführbar markiert
ist. Falls doch: `chmod +x gradlew`.

**Beim Start des Clients: „Deprecated Gradle features were used …“**
Bekannt und harmlos. Die Meldung stammt vom JavaFX-Plugin, nicht aus unserem
Code. Details in [`devops.md`](devops.md#411-bekannte-probleme).

**Fehler „projectDirectory … does not exist“ nach Änderung an den Modulen**
Neue Module gehören in die `settings.gradle.kts` im **Hauptordner**, nicht in
`buildSrc/settings.gradle.kts`. IntelliJ zeigt die beiden im Reiter als
`settings.gradle.kts (labyrinth)` bzw. `(buildSrc)`.
