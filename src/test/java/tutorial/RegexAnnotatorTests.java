package tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.junit.Ignore;
import org.junit.Test;
import tutorial.types.Text;

import java.util.Collection;

public class RegexAnnotatorTests {
    @Test
    public void addsTextAnnotationsWhenFound() throws Exception {
        // Arrange: Set document text on the CAS to our note and create RegexAnnotator AE that looks
        // for for mentions of trauma and loss of consciousness in note
        String note = "The patient has traumatic brain injury and has been experiencing loss of consciousness.";
        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText(note);
        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            RegexAnnotator.class,
            RegexAnnotator.PARAM_REGEX,
            "trauma|loss of consciousness"
        );
        aggregateBuilder.add(aeDescription);

        // Act: Run the pipeline
        // Note: CRs are usually mandatory, but you can actually run a SimplePipeline programmatically with just a jCas as we do
        //       here. If you do that, it will just run the pipeline on one document: the one you set by calling jCas.setDocumentText().
        //       We really only do this for testing purposes.
        SimplePipeline.runPipeline(jCas, aggregateBuilder.createAggregateDescription());

        // Assert: The RegexAnnotator should have found two text mentions for trauma|loss of consciousness
        int expectedSize = 2;
        Collection<Text> texts = JCasUtil.select(jCas, Text.class);
        assertEquals(expectedSize, texts.size());
        for (Text text : texts) {
            String actualText = note.substring(text.getBegin(), text.getEnd());
            String expectedText = text.getText();
            assertEquals(expectedText, actualText);
            assertTrue(actualText.equals("trauma") || actualText.equals("loss of consciousness"));
        }
    }
}
