package tutorial;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;
import tutorial.types.Text;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegexAnnotatorTests {
    public static final String TEST_NOTE = "The patient has traumatic brain injury and has been experiencing loss of consciousness.";

    @Test
    public void addsTextAnnotationsWhenFound() throws Exception {
        // Arrange: Set document text on the CAS to our note and create RegexAnnotator AE that looks
        // for for mentions of trauma and loss of consciousness in note
        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText(TEST_NOTE);
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
            String actualText = TEST_NOTE.substring(text.getBegin(), text.getEnd());
            String expectedText = text.getText();
            assertEquals(expectedText, actualText);
            assertTrue(actualText.equals("trauma") || actualText.equals("loss of consciousness"));
        }
    }
}
