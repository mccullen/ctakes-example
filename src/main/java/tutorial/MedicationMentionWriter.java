package tutorial;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class MedicationMentionWriter extends JCasConsumer_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(MedicationMentionWriter.class.getName());

    public static final String PARAM_OUTPUT_FILE = "OutputFile";
    @ConfigurationParameter(
        name = PARAM_OUTPUT_FILE,
        description = "The path to the file to write the output to",
        mandatory = true,
        defaultValue = ""
    )
    private String _outputFile;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing CC with OutputFile=" + _outputFile);
    }

    @Override
    public void process(JCas jCas) {
        LOGGER.info("Processing");
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
    }
}
