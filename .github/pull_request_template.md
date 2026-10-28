<!--
Titel im Commit-Format, englisch:  <typ>(<JIRA-KEY>): <was geändert wurde>
Beispiel:                           feat(AIP6-42): validate tile push on server
Der Titel wird beim Merge zum Commit auf main. Details: CONTRIBUTING.md
-->

## Was

<!-- Was ändert dieser PR? Ein bis drei Sätze reichen. -->

## Warum

<!-- Welches Problem löst er? Jira-Ticket verlinken. -->

Jira: AIP6-

## Checkliste

<!-- Definition of Done (Pflichtenheft 12.1, CONTRIBUTING.md).
Review und grüne Pipeline sind vor dem Merge Pflicht. -->

- [ ] Titel im Format `<typ>(<JIRA-KEY>): …`
- [ ] Lokal gebaut (`gradlew build`), Tests grün
- [ ] Tests für neue Logik, mit Anforderungs-ID (`@Tag("F-…")`)
- [ ] Javadoc für neue öffentliche Klassen und Methoden, auf Englisch
- [ ] Anforderungs-ID im Jira-Ticket verknüpft
- [ ] Doku angepasst, falls nötig
