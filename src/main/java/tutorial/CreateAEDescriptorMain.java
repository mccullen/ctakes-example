package tutorial;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;

import java.io.FileWriter;

public class CreateAEDescriptorMain {
    public static final String OUTPUT_FILE = "references/RegexAnnotatorDescriptor.xml";

    public static void main(String[] args) throws Exception {
        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            RegexAnnotator.class,
            RegexAnnotator.PARAM_REGEX,
            "trauma|loss of consciousness"
        );
        aggregateBuilder.add(aeDescription);
        aggregateBuilder.createAggregateDescription().toXML(new FileWriter(OUTPUT_FILE));
    }
}
