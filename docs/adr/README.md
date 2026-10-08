# Architekturentscheidungen (ADRs)

Hier halten wir Entscheidungen fest, die das Projekt langfristig prägen: was
entschieden wurde, warum, und welche Alternativen verworfen wurden. Wie und
warum wir das tun, steht in [ADR 0001](0001-architekturentscheidungen-festhalten.md).

| Nr. | Entscheidung | Status | Datum |
|---|---|---|---|
| [0001](0001-architekturentscheidungen-festhalten.md) | Architekturentscheidungen als ADRs festhalten | angenommen | 08.10.2026 |
| [0002](0002-ein-repository-mit-modulen.md) | Ein Repository mit Modulen | angenommen | 30.09.2026 |
| [0003](0003-gradle-als-build-werkzeug.md) | Gradle als Build-Werkzeug | angenommen | 30.09.2026 |
| [0004](0004-jdk-21-temurin.md) | JDK 21 (Eclipse Temurin) über die Gradle-Toolchain | angenommen | 30.09.2026 |

## Neue ADR anlegen

1. Nächste freie Nummer nehmen, Datei `NNNN-kurzer-titel.md` anlegen.
2. Vorlage unten kopieren und ausfüllen.
3. In die Tabelle oben eintragen.
4. Per Pull Request einreichen; die Diskussion im Review gehört zur
   Entscheidung.

Eine angenommene ADR wird nicht mehr umgeschrieben. Ändert sich die
Entscheidung, entsteht eine neue ADR, und die alte bekommt den Status
„ersetzt durch NNNN“.

## Vorlage

```markdown
# NNNN. Titel als Aussage

- Status: vorgeschlagen | angenommen | abgelehnt | ersetzt durch NNNN
- Datum: TT.MM.JJJJ
- Entschieden von: Gruppe | Rolle

## Kontext

Welche Situation, welches Problem, welche Randbedingungen?

## Entscheidung

Was wir tun.

## Begründung

Warum diese Lösung.

## Betrachtete Alternativen

Was sonst in Frage kam und warum nicht.

## Konsequenzen

Was dadurch leichter wird, was schwerer, was zu beachten ist.
```
