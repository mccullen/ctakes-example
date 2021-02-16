package tutorial;

import static org.junit.Assert.assertEquals;

import org.apache.ctakes.core.pipeline.PipelineBuilder;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.token.type.Sentence;
import org.junit.Test;
import tutorial.types.Text;

import java.util.Collection;

public class RegexAnnotatorTests {
    @Test
    public void testSimpleRegex() throws Exception {
        // Set document text
        String note = "The patient says they took 325 mg aspirin for knee pain.";
        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText(note);

        // Create pipeline using UimaFit
        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            RegexAnnotator.class,
            RegexAnnotator.PARAM_REGEX,
            "trauma|loss of consciousness"
        );
        aggregateBuilder.add(aeDescription);

        SimplePipeline.runPipeline(jCas, aggregateBuilder.createAggregateDescription());

        Collection<Sentence> sentences = JCasUtil.select(jCas, Sentence.class);

        Collection<Text> texts = JCasUtil.select(jCas, Text.class);
        assertEquals(texts.size(), 1);
    }
}
