package tutorial;

import org.apache.ctakes.core.config.ConfigParameterConstants;
import org.apache.ctakes.core.pipeline.PipelineBuilder;
import org.apache.ctakes.core.pipeline.PiperFileReader;

public class DefaultClinicalPipelineMain {
    // CHANGE THESE VALUES FOR YOUR ENVIRONMENT!!!
    public static final String PIPER_FILE = "C:/root/vdt/icapa/nlp/apache-ctakes-4.0.0/resources/org/apache/ctakes/clinical/pipeline/DefaultFastPipeline.piper";
    public static final String INPUT_DIR = "references/default-clinical-pipeline/notes/";
    public static final String OUTPUT_DIR = "references/default-clinical-pipeline/annotations/";
    public static final String UMLS_KEY = Util.getProperty("umls.key");

    public static void main(String[] args) throws Exception {
        PiperFileReader piperReader = new PiperFileReader();
        PipelineBuilder builder = piperReader.getBuilder();

        // Set the CLI parameters
        builder.set("umlsKey", UMLS_KEY);
        builder.set(ConfigParameterConstants.PARAM_INPUTDIR, INPUT_DIR);
        builder.set(ConfigParameterConstants.PARAM_PIPER, PIPER_FILE);

        // Add the default CR and CC
        builder.readFiles(INPUT_DIR);
        builder.writeXMIs(OUTPUT_DIR);

        // Load the piper file line by line
        piperReader.loadPipelineFile(PIPER_FILE);

        builder.run();
    }
}
