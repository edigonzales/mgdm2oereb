package ch.so.agi.mgdm2oereb;

import java.io.File;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "mgdm2oereb",
        description = "Transformiert MGDM-Datensätze in das ÖREB-Rahmenmodell.",
        //version = "mgdm2oereb version 0.0.1",
        mixinStandardHelpOptions = true,
        
        headerHeading = "%n",
        //synopsisHeading = "%n",
        descriptionHeading = "%nDescription: ",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n"
      )
public class App implements Callable<Integer> {

    @Option(names = { "-l", "--loglevel" }, required = false, defaultValue = "INFO", description = "Loglevel [ERROR | WARN | INFO | DEBUG | TRACE]")
    String loglevel;
    
    @ArgGroup(validate = false, heading = "%nTransformation:%n")
    TransformationOptions transformationOptions;

    static class TransformationOptions {
        @Option(names = { "-i", "--input" }, required = true, description = "MGDM input file that needs to be transformed.")
        File inputFile;
        
        @Option(names = { "-d", "--outputDirectory" }, required = false, description = "The directory where the transformed INTERLIS file will be stored.")
        File outputDirectory;

        @Option(names = { "-m", "--model" }, required = true, description = "INTERLIS data model of input file.") 
        String model;
        
        @Option(names = { "-t", "--themeCode" }, required = true, description = "ÖREB theme code of output file.") 
        String themeCode;
        
        @Option(names = { "-c", "--catalog" }, required = true, description = "Catalog file name with additional needed information.") 
        String catalogFileName;
        
        @Option(names = { "-v", "--validate" }, required = false, description = "Validate the transformed INTERLIS transfer file.") 
        boolean validate;
    }

    @ArgGroup(exclusive = false, heading = "%nOEREBlex:%n")
    OereblexOptions oereblexOptions;

    static class OereblexOptions {
        @Option(names = { "--oereblexHost" }, required = true, description = "Host of oereblex application (without protocol).") 
        String oereblexHost;
        
        @Option(names = { "--oereblexCanton" }, required = true, description = "Two letter abbrevation of canton.") 
        String oereblexCanton;

        @Option(names = { "--dummyOfficeName" }, required = true, description = "A dummy office name.") 
        String dummyOfficeName;
        
        @Option(names = { "--dummyOfficeUrl" }, required = true, description = "A dummy office url.") 
        String dummyOfficeUrl;
    }

    @Override
    public Integer call() throws Exception {     
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, loglevel);
        org.slf4j.Logger log = LoggerFactory.getLogger(App.class);

        Settings settings = new Settings();
        settings.setValue(Mgdm2Oereb.MODEL, transformationOptions.model);
        settings.setValue(Mgdm2Oereb.THEME_CODE, transformationOptions.themeCode);
        settings.setValue(Mgdm2Oereb.CATALOG, transformationOptions.catalogFileName);        
        settings.setValue(Mgdm2Oereb.VALIDATE, Boolean.toString(transformationOptions.validate));
        settings.setValue(Mgdm2Oereb.LOGLEVEL, loglevel);

        if (transformationOptions.outputDirectory == null) {
            transformationOptions.outputDirectory = transformationOptions.inputFile.getParentFile();
        }

        if (oereblexOptions != null) {
            settings.setValue(Mgdm2Oereb.OEREBLEX_HOST, oereblexOptions.oereblexHost);
            settings.setValue(Mgdm2Oereb.OEREBLEX_CANTON, oereblexOptions.oereblexCanton);
            settings.setValue(Mgdm2Oereb.DUMMY_OFFICE_URL, oereblexOptions.dummyOfficeName);
            settings.setValue(Mgdm2Oereb.DUMMY_OFFICE_NAME, oereblexOptions.dummyOfficeUrl);
        }

        boolean valid = false;

        Mgdm2Oereb mgdm2oereb = new Mgdm2Oereb();        
        valid = mgdm2oereb.convert(transformationOptions.inputFile.getAbsolutePath(), transformationOptions.outputDirectory.getAbsolutePath(), settings);

        return valid ? 0 : 1;
    }
    
    public static void main(String... args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
