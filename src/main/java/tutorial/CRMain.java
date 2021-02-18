package tutorial;

import org.apache.ctakes.core.pipeline.PipelineBuilder;
import org.apache.ctakes.core.pipeline.PiperFileReader;
import org.apache.ctakes.dictionary.lookup2.util.UmlsUserApprover;

public class CRMain {
    // CHANGE THESE VALUE TO MATCH YOUR ENVIRONMENT!!!
    public static final String PIPER_FILE = "references/cr/cr.piper";
    public static final String UMLS_KEY = Util.getProperty("umls.key");

    public static void main(String[] args) throws Exception {
        PiperFileReader piperReader = new PiperFileReader();
        PipelineBuilder builder = piperReader.getBuilder();

        // Set the parameters. You could alternatively set these in the piper file using the "set" command.
        builder.set(UmlsUserApprover.KEY_PARAM, UMLS_KEY);
        // Optional: Use a custom dictionary that you created using CTAKES_HOME/bin/runDictionaryCreator
        //builder.set(ConfigParameterConstants.PARAM_LOOKUP_XML, "path/to/lookup/xml");

        // Load the piper file line by line
        piperReader.loadPipelineFile(PIPER_FILE);

        builder.run();
    }
}
