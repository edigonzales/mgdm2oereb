# mgdm2oereb

Transformiert eine INTERLIS-Transferdatei eines MGDM in das ÖREB-Kataster-Rahmenmodell (Transferstruktur).

Für jedes Thema gibt es eine Transferdefinition und allenfalls eine zusätzliche Konfigurationsdatei, um fehlende Daten (z.B. Url des WMS) zu ergänzen.

Falls in den Daten "nur" ein ÖREBlex-Geolink erfasst ist, wird dieser aufgelöst und die Dokumente werden korrekt im Rahmenmodell erfasst.

## Benutzung

Die Anwendung wird auf der Kommandozeile gestartet:

```
./mgdm2oereb --help
```

Optionen für Transformation:

| Name | Beschreibung | Standard |
|-----|-----|-----|
| `--input` | Vollständiger, absoluter Pfad der Themebereitstellungs-Konfigurations-XML-Datei. | |
| `--outputDirectory` | Vollständiger, absoluter Pfad der Themebereitstellungs-Konfigurations-XML-Datei. | Verzeichnis der Inputdatei. |
| `--catalog` | Name der XTF-Datei (Datenmodell _OeREBKRMlegdrst_V2_0_) mit zusätzlichen Informationen, die für die Transformation benötigt werden. Die Datei selber liegt der Anwendung bei und muss lokal nicht vorliegen. | |
| `--model` | Datenmodell der Inputdatei (MGDM-Datenmodell). | |
| `--themeCode` | Code des ÖREB-Themas. Z.B "ch.Planungszonen" | |
| `--validate` | Die resultierende Datei wird auf Modellkonformität geprüft. | false |

Zusätzliche Optionen für Transformation mit ÖREBlex:

| Name | Beschreibung | Standard |
|-----|-----|-----|
| `--oereblexHost` | Hostname der ÖREBlex-Webanwendung. Ohne Protokoll (http:// resp. https://) | |
| `--oereblexCanton` | Abkürzung des Kantons. Z.B. "sh". | |
| `--dummyOfficeName` | Platzhalter für einen Office-Namen (aka zuständige Stelle) | |
| `--dummyOfficeUrl` | Platzhalter für eine Office-Url. | |

Falls eine ÖREBlex-Option verwendet wird, sind alle anderen zwingend.

Weitere Optionen:

| Name | Beschreibung | Standard |
|-----|-----|-----|
| `--loglevel` | Setzen des Loglevels. [ERROR, WARN, INFO, DEBUG] | INFO |

Beispielaufruf ohne ÖREBlex:

MGDM (`--inputFile`): https://raw.githubusercontent.com/edigonzales/mgdm2oereb/main/lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf

```
./mgdm2oereb --input=~/Downloads/ch.Planungszonen.sh.mgdm.v1_1.xtf --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```

Beispielaufruf mit ÖREBlex:

MGDM (`--inputFile`): https://raw.githubusercontent.com/edigonzales/mgdm2oereb/main/lib/src/test/data/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf

```
./mgdm2oereb --input=~/Downloads/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate --oereblexHost oereblex.sh.ch --oereblexCanton sh --dummyOfficeName DUMMY_OFFICE_NAME --dummyOfficeUrl https://interlis.ch
```

## ~~Konfigurieren und starten~~

## Externe Abhängigkeiten

## Konfiguration und Betrieb in der GDI

## Interne Struktur

## todo
~~- Wie handhaben, ob oereblex oder kein oereblex? Via CLI Parameter? https://picocli.info/#_argument_groups ?~~
- Output-Filename: Hardcodiert. Oder? Soll das so bleiben?
- Unterschiede beim Python-Skript dokumentieren
   * requests module (inkl. Fehlermeldung)
   * Klasse statt Skript -> nein, geht nicht wegen Interface. Aber trotzdem in eine Funktion gepackt.
- Fehlerhandling beim Download-Skript? Wenn z.B. das normale XTF Input ist, gibt es eine Decoding o.ä. Excecption.
- Clean Code:
  ~~* convert-Methoden: was kann man zusammenfassen?~~
- ~~if/else venv/bin/ dev vs prod depl~~
- ~~Kann man Python die Mgdm2Oereb-Keys bekanntmachen? -> Ja.~~
- Wie sinnvoll die resouce-config aus verschiedenen Testläufen mergen? Oder umgekehrt: wie kann man verschiede Testläufe mit dem Agent machen? Dito "{ "name": "ch.so.agi.mgdm2oereb.Settings", "allPublicMethods": true },": Wird nicht automatisch erkannt. https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html Testen mit Agent sollte doch möglich sein. Das Interface, welches ich manuell hinzufügte, fällt ja jetzt weg, weil es nicht geht (oder ich nicht verstehe).
- Eigentlich muss man die config.json trennen (app und lib). Aber wenn man es eh immer machen muss, wenn man eine Anwendung macht, kann man es auch dort lassen? 
- ~~!!Context pre-initialisieren!! -> nicht wirklich nötig, ist schnell im native-image (dünkt mich)~~. ~~Done. Gefühlt aber nicht noch schneller.~~
~~- Loglevel nicht via env var setzen im Python-Script.~~
- Tests

