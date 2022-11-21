# mgdm2oereb

Transformiert eine INTELRIS-Transferdatei eines MGDM in das ÖREB-Kataster-Rahmenmodell (Transferstruktur).

Für jedes Thema gibt es eine Transferdefinition und allenfalls eine zusätzliche Konfigurationsdatei, um fehlende Daten (z.B. Url des WMS) zu ergänzen.

Falls in den Daten "nur" ein ÖREBlex-Geolink erfasst ist, wird dieser aufgelöst und die Dokumente werden korrekt im Rahmenmodell erfasst.

## Komponenten

Die Anwendung besteht aus einer einzelnen Komponente.

## Benutzung

## Konfigurieren und starten

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

```
./app/build/distributions/mgdm2oereb-0.0.LOCALBUILD/bin/mgdm2oereb --input=./lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```


### GraalVM


#### Ohne OEREBlex
```
./gradlew app:shadowJar

java -agentlib:native-image-agent=config-output-dir=app/src/main/resources/META-INF/native-image -jar app/build/libs/app-0.0.LOCALBUILD-all.jar --input=lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```

```
./app/build/native/nativeCompile/mgdm2oereb --input=./lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```

#### Mit OEREBlex
```
./gradlew app:shadowJar

java -agentlib:native-image-agent=config-output-dir=app/src/main/resources/META-INF/native-image -jar app/build/libs/app-0.0.LOCALBUILD-all.jar --input=lib/src/test/data/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate --oereblexHost oereblex.sh.ch --oereblexCanton sh --dummyOfficeName DUMMY_OFFICE_NAME --dummyOfficeUrl https://google.ch
```

```
./app/build/native/nativeCompile/mgdm2oereb --input=lib/src/test/data/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate --oereblexHost oereblex.sh.ch --oereblexCanton sh --dummyOfficeName DUMMY_OFFICE_NAME --dummyOfficeUrl https://google.ch
```