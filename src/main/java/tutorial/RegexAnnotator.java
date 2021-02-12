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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        LOGGER.info("Initializing AE");
        _pattern = Pattern.compile(_regex);
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        LOGGER.info("Processing");

        Matcher matcher = _pattern.matcher(jCas.getDocumentText());
        while (matcher.find()) {
            Text text = new Text(jCas);
            text.setBegin(matcher.start());
            text.setEnd(matcher.end());
            text.setSize(matcher.end() - matcher.start());
            String subStr = jCas.getDocumentText().substring(matcher.start(), matcher.end());
            text.setText(subStr);
            text.addToIndexes();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
    }
}
