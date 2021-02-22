package tutorial;

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.MedicationMention;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class MedicationMentionWriterTests {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void writesMedicationCurrectly() throws Exception {
        // Arrange: Add a test medication mention with a UMLS concept to the cas and create
        // MedicationMentionWriter.
        JCas jCas = JCasFactory.createJCas();
        UmlsConcept umlsConcept = getTestUmlsConcept(jCas);
        MedicationMention medicationMention = getTestMedicationMention(jCas, umlsConcept);
        medicationMention.addToIndexes();
        AggregateBuilder aggregateBuilder = new AggregateBuilder();
        // Create temp file to write to. This will be destroyed after the test completes
        String outputFile = temporaryFolder.newFile("temp-medication-mentions.csv").toString();
        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createEngineDescription(
            MedicationMentionWriter.class,
            MedicationMentionWriter.PARAM_DOUBLE_QUOTES, false,
            MedicationMentionWriter.PARAM_OUTPUT_FILE, outputFile
        );
        aggregateBuilder.add(aeDescription);

        // Act: Run the pipeline with the MedicationMentionWriter
        SimplePipeline.runPipeline(jCas, aggregateBuilder.createAggregateDescription());

        // Assert: Make sure that the writer wrote our test medication mention correctly
        FileInputStream fileInputStream = new FileInputStream(outputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String expectedFirstLine = "medication_mention_address,begin,end,umls_concept_address,coding_scheme,code,cui,tui,preferredText";
        String actualFirstLine = reader.readLine();
        assertEquals(expectedFirstLine, actualFirstLine);
        String expectedSecondLine = medicationMention.getAddress() + "," +
            medicationMention.getBegin() + "," +
            medicationMention.getEnd() + "," +
            umlsConcept.getAddress() + "," +
            umlsConcept.getCodingScheme() + "," +
            umlsConcept.getCode() + "," +
            umlsConcept.getCui() + "," +
            umlsConcept.getTui() + "," +
            umlsConcept.getPreferredText();
        String actualSecondLine = reader.readLine();
        assertEquals(expectedSecondLine, actualSecondLine);
    }

    private UmlsConcept getTestUmlsConcept(JCas jCas) {
        UmlsConcept umlsConcept = new UmlsConcept(jCas);
        umlsConcept.setCui("C0004057");
        umlsConcept.setTui("T121");
        umlsConcept.setPreferredText("Aspirin");
        umlsConcept.setCode("1191");
        umlsConcept.setCodingScheme("RXNORM");
        return umlsConcept;
    }

    private MedicationMention getTestMedicationMention(JCas jCas, UmlsConcept umlsConcept) {
        MedicationMention medicationMention = new MedicationMention(jCas);
        medicationMention.setBegin(34);
        medicationMention.setEnd(41);
        medicationMention.getOntologyConceptArr();
        int fsArraySize = 1;
        FSArray ontologyConceptArr = new FSArray(jCas, fsArraySize);
        ontologyConceptArr.set(0, umlsConcept);
        medicationMention.setOntologyConceptArr(ontologyConceptArr);
        return medicationMention;
    }
}
