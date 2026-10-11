# 0005. Kein eigener Verzeichnisserver

- Status: angenommen
- Datum: 11.10.2026
- Entschieden von: gruppenübergreifend, für uns übernommen in der Besprechung am 11.10.2026
- Ersetzt teilweise: [0002](0002-ein-repository-mit-modulen.md) (Modul `directory-server`)

## Kontext

Laut Lastenheft (Abschnitt 2) melden sich Spielserver bei einem zentralen
Verzeichnisserver an, und Clients holen sich dort die Liste der verfügbaren
Server. ADR 0002 sah dafür ein eigenes Modul `directory-server` vor.

Server und Clients aller Gruppen sollen untereinander austauschbar sein
(Lastenheft, Abschnitt 5). Ein Verzeichnisserver pro Gruppe würde dem
entgegenstehen: Ein Client fände nur die Server seiner eigenen Gruppe.

## Entscheidung

Wir bauen **keinen eigenen Verzeichnisserver**. Es gibt einen **gemeinsamen
Verzeichnisserver für alle Gruppen**, der außerhalb unseres Projekts entsteht.

- Das Modul `directory-server` wird aus dem Repository entfernt.
- `server` meldet sich beim gemeinsamen Verzeichnisserver an.
- `client-desktop` holt dort die Liste der Spielserver.
- Die Nachrichten dafür gehören in `protocol`, sobald ihr Format feststeht.

## Begründung

- Nur ein gemeinsames Verzeichnis macht die gruppenübergreifende
  Austauschbarkeit praktisch nutzbar.
- Weniger Code, Tests und Betrieb in unserem Projekt.

## Betrachtete Alternativen

- **Eigener Verzeichnisserver wie in ADR 0002 geplant:** würde nur unsere
  eigenen Server kennen und doppelte Arbeit zum gemeinsamen bedeuten.

## Konsequenzen

- Wir hängen von Format, Adresse und Verfügbarkeit des gemeinsamen
  Verzeichnisservers ab. Bis er steht, testen wir lokal mit einer festen
  Serveradresse oder einem einfachen Testdouble.
- Die Adresse des Verzeichnisservers muss in Server und Client einstellbar sein.
- Das Projekt hat jetzt vier Module: `protocol`, `core`, `server`,
  `client-desktop`.
