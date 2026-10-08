# 0001. Architekturentscheidungen als ADRs festhalten

- Status: angenommen
- Datum: 08.10.2026
- Entschieden von: Technikverantwortung

## Kontext

Im Projekt fallen früh Entscheidungen, die alle späteren Arbeiten prägen:
Repository-Aufbau, Build-Werkzeug, Java-Version, später Protokoll und
Architektur von Server und Client. Das Lastenheft verlangt, die Umsetzung
„durch geeignete Entwurfsdokumente nachvollziehbar zu begründen“
(Abschnitt 6).

Entscheidungen, die nur in Besprechungen oder Chats getroffen werden, sind nach
wenigen Wochen nicht mehr nachvollziehbar: Man weiß noch, *was* gilt, aber
nicht mehr, *warum*, und welche Alternativen schon verworfen wurden.

## Entscheidung

Wesentliche Entscheidungen werden als **Architecture Decision Records (ADRs)**
im Ordner `docs/adr/` festgehalten: eine kurze Markdown-Datei pro
Entscheidung, fortlaufend nummeriert, mit Kontext, Entscheidung, Begründung,
Alternativen und Konsequenzen. Vorlage und Übersicht in
[`README.md`](README.md).

Abgrenzung: ADRs beschreiben das **Warum**. Wie etwas aktuell eingerichtet ist,
steht in den Handbüchern (z. B. [`devops.md`](../devops.md)).

## Begründung

- Liegt im Repository neben dem Code und wird wie Code per Pull Request
  geprüft. Die Review-Diskussion wird Teil der Entscheidung.
- Wenig Aufwand: eine Seite pro Entscheidung, keine eigenen Werkzeuge.
- Weit verbreitetes Format (nach Michael Nygard).
- Liefert Material für Pflichtenheft und Abschlusspräsentation.

## Betrachtete Alternativen

- **Ein großes Entwurfsdokument:** veraltet schnell, Änderungen sind schwer
  nachzuvollziehen, die Entstehungsgeschichte geht verloren.
- **Jira oder Confluence:** getrennt vom Code, nach Projektende nicht
  zuverlässig verfügbar.
- **Nur Besprechungsprotokolle:** Entscheidungen gehen zwischen anderen Themen
  unter.

## Konsequenzen

- Wer eine weitreichende Änderung vorschlägt, schreibt dazu eine ADR.
- Angenommene ADRs werden nicht umgeschrieben, sondern bei Bedarf durch eine
  neue ersetzt. Die Historie bleibt vollständig.
- Die Entscheidungen vom 30.09.2026 (ADR 0002 bis 0004) wurden nachträglich
  festgehalten.
