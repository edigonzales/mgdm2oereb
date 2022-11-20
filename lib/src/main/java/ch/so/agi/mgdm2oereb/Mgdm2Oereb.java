package ch.so.agi.mgdm2oereb;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;

import org.interlis2.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class Mgdm2Oereb {
    //static Logger log = LoggerFactory.getLogger(Mgdm2Oereb.class);
    
    private static final String PYTHON = "python";

    private static final String SOURCE_FILE_NAME = "xsl/oereblex.download.py";
    
    public void convertWithPy(String inputXtfFileName, String outputDirectory, Settings settings) throws Mgdm2OerebException {
        var outDirectory = new File(outputDirectory);

        
        try {
            var geoLinkXslFileName = settings.getValue(Mgdm2Oereb.MODEL) + ".oereblex.geolink_list.xsl";
            var geoLinkXslFile = Paths.get(outDirectory.getAbsolutePath(), geoLinkXslFileName).toFile();
            Util.loadFile("xsl/"+geoLinkXslFileName, geoLinkXslFile);  
            
            settings.setValue(Mgdm2Oereb.GEOLINK_LIST_TRAFO_PATH, geoLinkXslFile.getAbsolutePath());
        } catch (IOException e) {
            throw new Mgdm2OerebException(e.getMessage());
        }

        //TODO if/else (dev vs prod(jar))
        var venvExePath = Mgdm2Oereb.class.getClassLoader()
                .getResource(Paths.get("venv", "bin", "graalpy").toString())
                .getPath();
        
        var code = new InputStreamReader(Mgdm2Oereb.class.getClassLoader().getResourceAsStream(SOURCE_FILE_NAME));

        try (var context = Context.newBuilder("python")
                .allowAllAccess(true)
                .option("python.Executable", venvExePath)
                .option("python.ForceImportSite", "true")
                .build()) {
            
            System.out.println("Hallo GraalVM");
            context.eval(Source.newBuilder(PYTHON, code, SOURCE_FILE_NAME).build());


            Value pyOereblexDownloaderClass = context.getPolyglotBindings().getMember("OereblexDownloader");
            Value pyOereblexDownloader = pyOereblexDownloaderClass.newInstance();

            OereblexDownloader oereblexDownloader = pyOereblexDownloader.as(OereblexDownloader.class);
            oereblexDownloader.run(new File(inputXtfFileName).getAbsolutePath(), outDirectory.getAbsolutePath(), settings);

        } catch (IOException e) {
            throw new Mgdm2OerebException(e.getMessage());
        }

    } 
    
    

    public boolean convert(String inputXtfFileName, String outputDirectory, Settings settings) throws Mgdm2OerebException {        
        var outDirectory = new File(outputDirectory);
        
        try {
            var xslFileName = settings.getValue(Mgdm2Oereb.MODEL) + ".trafo.xsl";
            var xslFile = Paths.get(outDirectory.getAbsolutePath(), xslFileName).toFile();
            Util.loadFile("xsl/"+xslFileName, xslFile);
            
            var outputXtfFile = Paths.get(outDirectory.getAbsolutePath(), "OeREBKRMtrsfr_V2_0.xtf").toFile();

            var catalogFileName = settings.getValue(Mgdm2Oereb.CATALOG);
            var catalogFile = Paths.get(outDirectory.getAbsolutePath(), catalogFileName).toFile();
            Util.loadFile("catalogs/"+catalogFileName, catalogFile);
            
            Processor proc = new Processor(false);
            XsltCompiler comp = proc.newXsltCompiler();
            XsltExecutable exp = comp.compile(new StreamSource(xslFile));
            
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(new File(inputXtfFileName)));
            Serializer outXtf = proc.newSerializer(outputXtfFile);
            XsltTransformer trans = exp.load();
            trans.setInitialContextNode(source);
            trans.setDestination(outXtf);
            trans.setParameter(new QName("theme_code"), (XdmValue) XdmAtomicValue.makeAtomicValue(settings.getValue(Mgdm2Oereb.THEME_CODE)));
            trans.setParameter(new QName("model"), (XdmValue) XdmAtomicValue.makeAtomicValue(settings.getValue(Mgdm2Oereb.MODEL)));        
            trans.setParameter(new QName("catalog"), (XdmValue) XdmAtomicValue.makeAtomicValue(catalogFile.getAbsolutePath()));
            trans.transform();
            trans.close();
            
            boolean validate = Boolean.parseBoolean(settings.getValue(Mgdm2Oereb.VALIDATE));
            
            if (validate) {
                var iliSettings = new ch.ehi.basics.settings.Settings();
                boolean valid = Validator.runValidation(outputXtfFile.getAbsolutePath(), iliSettings);
                return valid;
            }
            
        } catch (IOException | SaxonApiException e) {
            throw new Mgdm2OerebException(e.getMessage());
        }
        return true;
    }

    // TODO: Woher / wie kennt Python diese? HostExport o.ä.?
    public static final String MODEL = "MODEL"; 
    
    public static final String THEME_CODE = "THEME_CODE";
    
    public static final String OEREBLEX_HOST = "OEREBLEX_HOST";

    public static final String OEREBLEX_CANTON = "OEREBLEX_CANTON";
    
    public static final String GEOLINK_LIST_TRAFO_PATH = "GEOLINK_LIST_TRAFO_PATH";

    public static final String CATALOG = "CATALOG";
       
    public static final String DUMMY_OFFICE_NAME = "DUMMY_OFFICE_NAME";
    
    public static final String DUMMY_OFFICE_URL = "DUMMY_OFFICE_URL";
    
    public static final String VALIDATE = "VALIDATE";

}
