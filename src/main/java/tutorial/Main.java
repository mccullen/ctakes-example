package tutorial;

import org.apache.ctakes.core.config.ConfigParameterConstants;
import org.apache.ctakes.core.pipeline.PipelineBuilder;
import org.apache.ctakes.core.pipeline.PiperFileReader;

public class Main {
    public static final String PIPER_FILE = "C:/root/vdt/icapa/nlp/apache-ctakes-4.0.0/resources/org/apache/ctakes/clinical/pipeline/DefaultFastPipeline.piper";
    public static final String INPUT_DIR = "C:/root/vdt/icapa/nlp/apache-ctakes-4.0.0/testdata/notes/100/";
    public static final String OUTPUT_DIR = "C:/root/";
    public static final String UMLS_KEY = "08cde565-a6b0-4a50-8035-1a3d6ceb3835";

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
