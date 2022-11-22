
### [move to develop / Interne Struktur] GraalVM


#### [move to develop / Interne Struktur] Ohne OEREBlex
```
./gradlew app:shadowJar

java -agentlib:native-image-agent=config-output-dir=app/src/main/resources/META-INF/native-image -jar app/build/libs/app-0.0.LOCALBUILD-all.jar --input=lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```

```
./app/build/native/nativeCompile/mgdm2oereb --input=./lib/src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate
```

#### [move to develop / Interne Struktur] Mit OEREBlex
```
./gradlew app:shadowJar

java -agentlib:native-image-agent=config-output-dir=app/src/main/resources/META-INF/native-image -jar app/build/libs/app-0.0.LOCALBUILD-all.jar --input=lib/src/test/data/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate --oereblexHost oereblex.sh.ch --oereblexCanton sh --dummyOfficeName DUMMY_OFFICE_NAME --dummyOfficeUrl https://google.ch
```

```
./app/build/native/nativeCompile/mgdm2oereb --input=lib/src/test/data/ch.Planungszonen.sh.mgdm_oereblex.v1_1.xtf --outputDirectory=/Users/stefan/tmp/mgdm2oereb/ --model=Planungszonen_V1_1 --themeCode=ch.Planungszonen --catalog=ch.sh.OeREBKRMkvs_supplement.xml --validate --oereblexHost oereblex.sh.ch --oereblexCanton sh --dummyOfficeName DUMMY_OFFICE_NAME --dummyOfficeUrl https://google.ch
```