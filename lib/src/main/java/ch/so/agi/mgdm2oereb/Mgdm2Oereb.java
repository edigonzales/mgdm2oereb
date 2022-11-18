package ch.so.agi.mgdm2oereb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mgdm2Oereb {
    static Logger log = LoggerFactory.getLogger(Mgdm2Oereb.class);

    public void convert(String xtfFileName, Settings settings) throws IOException {        
        var outputDirectory = new File(xtfFileName).getParentFile();
        
        var xslFileName = settings.getValue(Mgdm2Oereb.MODEL) + ".trafo.xsl";
        var xslFile = Paths.get(outputDirectory.getAbsolutePath(), xslFileName).toFile();
        Util.loadFile(xslFileName, xslFile);
                
        log.info("fubar*****");
        
//        var resultXtfFile = Paths.get(outDirectory, "result.html").toFile();
//        
//        String XML2PDF_XSL = "xml2html.xsl";
//        File xsltFile = new File(Paths.get(outDirectory, XML2PDF_XSL).toFile().getAbsolutePath());
//        InputStream xsltFileInputStream = Validator.class.getResourceAsStream("/"+XML2PDF_XSL); 
//        Files.copy(xsltFileInputStream, xsltFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        xsltFileInputStream.close();
//
//        Processor proc = new Processor(false);
//        XsltCompiler comp = proc.newXsltCompiler();
//        XsltExecutable exp = comp.compile(new StreamSource(xsltFile));
//        
//        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(resultXmlFile));
//        Serializer outHtml = proc.newSerializer(resultXtfFile);
//        XsltTransformer trans = exp.load();
//        trans.setInitialContextNode(source);
//        trans.setDestination(outHtml);
//        trans.transform();
//        trans.close();

        
        
    }
    
    
    
    public static final String MODEL = "MODEL"; 
    
    public static final String THEME_CODE = "THEME_CODE";
    
    public static final String OEREBLEX_HOST = "OEREBLEX_HOST";
    
    //public static final String XTF_FILE = "XTF_FILE";
     
    public static final String CATALOG = "CATALOG";
   
    public static final String OEREBLEX_CANTON = "OEREBLEX_CANTON";
    
    public static final String DUMMY_OFFICE_NAME = "DUMMY_OFFICE_NAME";
    
    public static final String DUMMY_OFFICE_URL = "DUMMY_OFFICE_URL";

}
