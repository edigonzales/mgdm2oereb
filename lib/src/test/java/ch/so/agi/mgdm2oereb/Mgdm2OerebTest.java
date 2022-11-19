package ch.so.agi.mgdm2oereb;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sf.saxon.s9api.SaxonApiException;

import org.interlis2.validator.Validator;

import static org.junit.jupiter.api.Assertions.*;

class Mgdm2OerebTest {
    @Test 
    void convertWithoutOereblex(@TempDir Path tempDir) throws Mgdm2OerebException {
        var settings = new Settings();
        settings.setValue(Mgdm2Oereb.MODEL, "Planungszonen_V1_1");
        settings.setValue(Mgdm2Oereb.THEME_CODE, "ch.Planungszonen");
        settings.setValue(Mgdm2Oereb.CATALOG, "ch.sh.OeREBKRMkvs_supplement.xml");
        settings.setValue(Mgdm2Oereb.VALIDATE, Boolean.toString(true));
        
        var tempDirectory = tempDir.toFile();
        
        Mgdm2Oereb mgdm2oereb = new Mgdm2Oereb();
        mgdm2oereb.convert("src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf", tempDirectory.getAbsolutePath(), settings);
        
        var iliSettings = new ch.ehi.basics.settings.Settings();
        var outputFileName = Paths.get(tempDirectory.getAbsolutePath(), "OeREBKRMtrsfr_V2_0.xtf").toFile();
        boolean valid = Validator.runValidation(outputFileName.getAbsolutePath(), iliSettings);
        assertTrue(valid);
    }
}