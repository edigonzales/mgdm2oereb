package ch.so.agi.mgdm2oereb;

import java.io.File;
import java.util.concurrent.Callable;

import picocli.CommandLine;
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

    @Option(names = { "-v", "--validate" }, required = false, description = "Validate transformed INTERLIS transfer file.") 
    boolean validate;

    @Override
    public Integer call() throws Exception {
        // TODO Auto-generated method stub
        
        Settings settings = new Settings();
        settings.setValue(Mgdm2Oereb.MODEL, model);
        settings.setValue(Mgdm2Oereb.THEME_CODE, themeCode);
        settings.setValue(Mgdm2Oereb.CATALOG, catalogFileName);
        settings.setValue(Mgdm2Oereb.VALIDATE, Boolean.toString(validate));
        
        if (outputDirectory == null) {
            outputDirectory = inputFile.getParentFile();
        }
        
        Mgdm2Oereb mgdm2oereb = new Mgdm2Oereb();
        boolean failed = mgdm2oereb.convert(inputFile.getAbsolutePath(), outputDirectory.getAbsolutePath(), settings);
        
        return failed ? 1 : 0;
    }
    
    public static void main(String... args) {
        int exitCode = new CommandLine(new App())
                .execute(args);
        System.exit(exitCode);
    }
}
