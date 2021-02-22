package tutorial;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {

        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText("");
        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            RegexAnnotator.class,
            RegexAnnotator.PARAM_REGEX,
            "trauma|loss of consciousness"
        );
        aggregateBuilder.add(aeDescription);
        aggregateBuilder.createAggregateDescription().toXML(new FileWriter("temp.xml"));
    }
}
