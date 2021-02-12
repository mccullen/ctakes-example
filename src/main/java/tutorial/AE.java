package tutorial;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.util.Collection;

public class AE extends JCasAnnotator_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(AE.class.getName());

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing AE");
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        LOGGER.info("Processing");
        Test test = new Test(jCas);
        test.addToIndexes();
        Collection<Test> tests = JCasUtil.select(jCas, Test.class);
        for (Test t : tests) {
            System.out.println(t);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
    }
}
