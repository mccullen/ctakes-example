package tutorial;

import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;
import tutorial.types.Text;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MedicationMentionWriterTests {
    @Test
    public void testSimpleRegex() throws Exception {
        // Arrange: Set document text on the CAS to our note and create RegexAnnotator AE that looks
        // for for mentions of trauma and loss of consciousness in note
        String note = "The patient says they took 325 mg aspirin for knee pain.";
        JCas jCas = JCasFactory.createJCas();
        jCas.setDocumentText(note);

        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        // Add the default clinical pipeline programatically
        AnalysisEngineDescription defaultClinicalPipeline = ClinicalPipelineFactory.getDefaultPipeline();
        aggregateBuilder.add(defaultClinicalPipeline);
        // Add our MedicationMentionWriter CC after that
        String outputFile = "";
        File f = new File("src/test/resources/documents.txt");

        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            MedicationMentionWriter.class,
            MedicationMentionWriter.PARAM_DOUBLE_QUOTES, "false",
            MedicationMentionWriter.PARAM_OUTPUT_FILE, outputFile
        );
        aggregateBuilder.add(aeDescription);

        // Act: Run the pipeline
        // Note: CRs are usually mandatory, but you can actually run a SimplePipeline programmatically with just a jCas as we do
        //       here. If you do that, it will just run the pipeline on one document: the one you set by calling jCas.setDocumentText().
        //       We really only do this for testing purposes.
        SimplePipeline.runPipeline(jCas, aggregateBuilder.createAggregateDescription());
        // If you didn't want to use an aggregate builder, you could also do this. Just FYI...
        // SimplePipeline.runPipeline(jCas, defaultClinicalPipeline, aeDescription);

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
