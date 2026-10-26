# 0006. Jira und GitHub über den Ticket-Key verknüpfen

- Status: angenommen
- Datum: 25.10.2026
- Entschieden von: Technikverantwortung

## Kontext

Aufgaben, User Stories und Sprints verwalten wir in Jira, Code und Pull
Requests in GitHub (`CONTRIBUTING.md`, Abschnitt „Wo was liegt“). Ohne
Verbindung müsste man im Ticket von Hand festhalten, welcher Branch und welcher
Pull Request dazugehören, und das passiert erfahrungsgemäß nicht.

Branch-Namen und Commit-Nachrichten enthalten laut `CONTRIBUTING.md` ohnehin
schon den Jira-Key. Gefehlt hat die technische Anbindung und ein echter Key
statt des Platzhalters `LAB`.

## Entscheidung

- Jira wird über die offizielle App **„GitHub for Atlassian“** mit der
  Organisation `mci-dibse-labyrinth-ws26` verbunden.
- Die App bekommt nur Zugriff auf das Repository **`labyrinth`**.
- Der Projekt-Key ist **`AIP6`**. Er steht wie bisher geregelt im Branch-Namen
  (`<typ>/AIP6-<nr>-<kurzbeschreibung>`), in Commit-Nachrichten und im PR-Titel
  (`<typ>(AIP6-<nr>): <beschreibung>`).
- **Smart Commits nutzen wir nicht.** Ticket-Status ändert man in Jira, nicht
  über Commit-Nachrichten.

## Begründung

- Branch, Commits und Pull Request erscheinen automatisch im Ticket. Jira zeigt
  so den echten Stand, ohne zusätzliche Pflege.
- Die Verknüpfung baut auf der bestehenden Konvention auf. Für das Team ändert
  sich nur der Key.
- Zugriff nur auf ein Repository folgt dem Grundsatz, Apps nicht mehr Rechte
  zu geben als nötig.
- Ohne Smart Commits verschiebt kein Tippfehler in einer Commit-Nachricht ein
  Ticket. Den Status pflegt bewusst ein Mensch in Jira.

## Betrachtete Alternativen

- **Keine Anbindung, Key nur als Konvention:** Die Historie bliebe nach Key
  durchsuchbar, aber im Ticket wäre nichts zu sehen.
- **GitHub Issues statt Jira:** verworfen, weil die Gruppe mit Jira plant. Zwei
  Tracker würden auseinanderlaufen; GitHub Issues sind abgeschaltet.
- **Smart Commits:** spart Klicks in Jira, verschiebt aber Tickets auch bei
  Fehlern und funktioniert nur, wenn die Commit-E-Mail einem Jira-Konto
  zugeordnet ist.

## Konsequenzen

- Ohne Key im Branch-Namen oder PR-Titel taucht die Arbeit nicht im Ticket auf.
- Um die Verbindung zu ändern oder neu zu autorisieren, braucht es Jira-Admin-
  und GitHub-Owner-Rechte gleichzeitig.
- Wird der Key in Jira je geändert, zeigen ältere Branches und Commits auf den
  alten Key. Der Key bleibt deshalb fest.
