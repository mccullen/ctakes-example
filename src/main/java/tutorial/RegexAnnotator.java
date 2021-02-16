package tutorial;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import tutorial.types.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a an analysis engine so we are extending JCasAnnotator_ImplBase
 * */
public class RegexAnnotator extends JCasAnnotator_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(RegexAnnotator.class.getName());

    public static final String PARAM_REGEX = "Regex";
    @ConfigurationParameter(
        name = PARAM_REGEX,
        description = "The regular expression pattern to match",
        mandatory = true,
        defaultValue = ""
    )
    private String _regex;

    private Pattern _pattern;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing AE with Regex=" + _regex);
        _pattern = Pattern.compile(_regex);
    }

    @Override
    public void process(JCas jCas) {
        LOGGER.info("Processing");

        Matcher matcher = _pattern.matcher(jCas.getDocumentText());
        while (matcher.find()) {
            LOGGER.info("Found match. Adding Text type to CAS");

            // Create type
            Text text = new Text(jCas);

            // Set properties
            text.setBegin(matcher.start());
            text.setEnd(matcher.end());
            text.setSize(matcher.end() - matcher.start());
            String subStr = jCas.getDocumentText().substring(matcher.start(), matcher.end());
            text.setText(subStr);

            // Add to CAS index
            text.addToIndexes();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
    }
}
