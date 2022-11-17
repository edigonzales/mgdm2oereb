package ch.so.agi.mgdm2oereb;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Mgdm2OerebTest {
    @Test 
    void convertPlanungszonen() {
        Settings settings = new Settings();
        settings.setValue(Mgdm2Oereb.MODEL, "Planungszonen_V1_1");
        settings.setValue(Mgdm2Oereb.THEME_CODE, "ch.Planungszonen");
        //settings.setValue(Mgdm2Oereb.XTF_FILE, "ch.Planungszonen.sh.mgdm.v1_1.xtf");
        settings.setValue(Mgdm2Oereb.CATALOG, "ch.sh.OeREBKRMkvs_supplement.xml");
        
        Mgdm2Oereb mgdm2oereb = new Mgdm2Oereb();
        mgdm2oereb.convert("src/test/data/ch.Planungszonen.sh.mgdm.v1_1.xtf", settings);
    }
}
