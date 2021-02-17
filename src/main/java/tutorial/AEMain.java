package tutorial;

import org.apache.ctakes.core.config.ConfigParameterConstants;
import org.apache.ctakes.core.pipeline.PipelineBuilder;
import org.apache.ctakes.core.pipeline.PiperFileReader;

public class AEMain {
    // CHANGE THESE VALUE TO MATCH YOUR ENVIRONMENT!!!
    public static final String PIPER_FILE = "references/ae/ae.piper";
    public static final String UMLS_KEY = Util.getProperty("umls.key");

    public static void main(String[] args) throws Exception {
        PiperFileReader piperReader = new PiperFileReader();
        PipelineBuilder builder = piperReader.getBuilder();

        // Set the CLI parameters
        builder.set("umlsKey", UMLS_KEY);
        builder.set(ConfigParameterConstants.PARAM_PIPER, PIPER_FILE);

        // Load the piper file line by line
        piperReader.loadPipelineFile(PIPER_FILE);

        builder.run();
    }
}
