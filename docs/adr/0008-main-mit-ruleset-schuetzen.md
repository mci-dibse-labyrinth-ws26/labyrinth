# 0008. `main` mit einem Ruleset schützen

- Status: angenommen
- Datum: 11.11.2026
- Entschieden von: Technikverantwortung

## Kontext

Das Pflichtenheft v1.2 verlangt einen geschützten Hauptzweig: Änderungen nur
per Pull Request, Squash-Merge nach mindestens einer fremden Freigabe, Merge
nur bei grüner Pipeline (12.1, 12.4, Definition of Done). `CONTRIBUTING.md`
beschreibt das, technisch durchgesetzt war es bisher nicht. Jede Person mit
Schreibrecht konnte direkt auf `main` pushen.

Der Schutz war bewusst aufgeschoben: Solange nur eine Person am Repository
arbeitete, hätte die Review-Pflicht sie selbst blockiert. Seit dem
Team-Kickoff haben alle Gruppenmitglieder Schreibrecht und das Projekt lokal
gebaut.

## Entscheidung

- Ein **Ruleset** `protect-main` für den Standard-Branch (`main`):
  - Änderungen nur per Pull Request, **eine Freigabe** durch eine andere Person
  - Pflicht-Prüfung `build` muss grün sein ([ADR 0007](0007-ci-mit-github-actions.md))
  - offene Review-Kommentare müssen erledigt sein
  - nur Squash-Merge
  - `main` kann weder gelöscht noch per Force-Push überschrieben werden
- **Notausgang:** Die Rolle *Organization admin* steht in der Bypass-Liste,
  beschränkt auf *For pull requests only*.
- **Kein `CODEOWNERS`.**

Die einzelnen Einstellungen stehen in [`devops.md`, Abschnitt 6](../devops.md#6-branch-schutz).

## Begründung

- Die Regel aus Pflichtenheft und Definition of Done gilt damit technisch und
  nicht nur auf dem Papier.
- **Ruleset statt klassischer Branch-Schutzregel:** Ausnahmen lassen sich auf
  Pull Requests beschränken, und alle mit Lesezugriff sehen die Regeln unter
  *Rules* im Repository.
- **Eine Freigabe** verlangt das Pflichtenheft. Mehr würde bei fünf
  entwickelnden Personen bremsen.
- **Freigaben bleiben bei Nachträgen gültig:** Kleine Korrekturen nach dem
  Review sollen nicht erneut auf eine Freigabe warten. Die Pipeline prüft
  trotzdem jeden neuen Stand.
- **Branch muss nicht auf dem neuesten Stand von `main` sein:** Die Pipeline
  baut bei einem Pull Request das Ergebnis nach dem Merge
  ([`devops.md`, Abschnitt 5.2](../devops.md#52-wann-sie-läuft)) und nach dem
  Merge noch einmal auf `main`.
- **Notausgang nur über Pull Requests:** Eine Abgabe darf nicht daran
  scheitern, dass niemand für ein Review erreichbar ist oder die Pipeline wegen
  einer Störung bei GitHub nicht läuft. Die Ausnahme bleibt als Pull Request
  nachvollziehbar; direkt auf `main` pushen kann auch ein Owner nicht.
  Zugeordnet ist sie der Rolle, damit sie auch funktioniert, wenn ein Owner
  nicht erreichbar ist.
- **Kein `CODEOWNERS`:** Das Pflichtenheft verlangt es nicht. Eine
  Pflicht-Freigabe durch eine bestimmte Person wird schnell zum Engpass, etwa
  bei Krankheit oder kurz vor einer Abgabe. Wer einen Pull Request öffnet,
  wählt selbst jemanden, der den Bereich kennt.

## Betrachtete Alternativen

- **Klassische Branch-Schutzregel:** dieselben Prüfungen, aber wer dort
  ausgenommen ist, darf auch direkt pushen.
- **Keine Ausnahme, auch nicht für Owner:** am strengsten, aber ohne Ausweg,
  wenn vor einer Abgabe niemand reviewen kann. Owner müssten dann das Ruleset
  abschalten, was schwerer nachzuvollziehen ist als ein Pull Request.
- **`CODEOWNERS`** (`protocol/` → Schnittstellenvertretung, `docs/` →
  Qualitätsmanagement): bisher in `devops.md` vorgesehen, aus den oben
  genannten Gründen verworfen. Lässt sich mit einer neuen ADR nachrüsten.
- **Zwei Freigaben:** mehr Sicherheit, bremst bei dieser Teamgröße zu stark.

## Konsequenzen

- Niemand pusht mehr direkt auf `main`, auch Owner nicht. Jede Änderung
  braucht Branch, Pull Request und eine zweite Person.
- Wer einen Pull Request öffnet, bittet aktiv jemanden ums Review. Reviews
  werden zeitnah erledigt, sonst stauen sich die Pull Requests.
- Der Job-Name `build` bleibt fest; wer ihn ändert, passt das Ruleset an.
- Wer den Notausgang nutzt, begründet das im Pull Request.
