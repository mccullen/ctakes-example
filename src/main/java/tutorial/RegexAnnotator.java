package tutorial;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import tutorial.types.Text;

import java.util.Collection;

public class RegexAnnotator extends JCasAnnotator_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(RegexAnnotator.class.getName());

    public static final String PARAM_PATTERN = "Pattern";
    @ConfigurationParameter(
        name = PARAM_PATTERN,
        description = "The regular expression pattern to match",
        mandatory = true,
        defaultValue = ""
    )
    private String _pattern;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing AE");
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        LOGGER.info("Processing");
        Text text = new Text(jCas);
        text.setText("hello");
        text.setSize(5);
        text.addToIndexes();
        Collection<Text> texts = JCasUtil.select(jCas, Text.class);
        for (Text t : texts) {
            System.out.println(t);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
    }
}
