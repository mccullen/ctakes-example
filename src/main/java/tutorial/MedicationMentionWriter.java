package tutorial;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.MedicationMention;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class MedicationMentionWriter extends JCasConsumer_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(MedicationMentionWriter.class.getName());

    public static final String PARAM_OUTPUT_FILE = "OutputFile";
    @ConfigurationParameter(
        name = PARAM_OUTPUT_FILE,
        description = "The path to the file to write the output to",
        mandatory = true,
        defaultValue = ""
    )
    private String _outputFile;

    public static final String PARAM_DOUBLE_QUOTES = "DoubleQuotes";
    @ConfigurationParameter(
        name = PARAM_DOUBLE_QUOTES,
        description = "Wheather we want the attributes to be wrapped in double quotes. For example, " +
        "do you want your row to be like \"123\",\"RXNORM\" or 123,RXNORM",
        mandatory = false,
        defaultValue = "false"
    )
    private boolean _doubleQuotes;

    private ICSVWriter _writer;
    private String[] _headers = {
        "medication_mention_address",
        "begin",
        "end",
        "umls_concept_address",
        "coding_scheme",
        "code",
        "cui",
        "tui",
        "preferredText"
    };

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing CC with OutputFile=" + _outputFile);
        setWriter();
        // Write the header row
        _writer.writeNext(_headers, _doubleQuotes);
    }

    private void setWriter() {
        try {
            File file = new File(_outputFile);
            // Set append to false so that, if the file exists, we will override it rather than append to it.
            boolean append = false;
            FileWriter fileWriter = new FileWriter(file, append);
            _writer = new CSVWriterBuilder(fileWriter).withSeparator(',').build();
        } catch (IOException e) {
            LOGGER.error("Error creating csv writer with OutputFile=" + _outputFile, e);
        }
    }

    @Override
    public void process(JCas jCas) {
        LOGGER.info("Processing");

        // Get all MedicationMentions that have been put in the jCas by previous UIMA components
        Collection<MedicationMention> medicationMentions = JCasUtil.select(jCas, MedicationMention.class);

        // Add all UmlsConcepts from within each MedicationMention to the CSV file
        medicationMentions.forEach(medicationMention -> {
            // Get properties off the medication mention
            int medicationMentionAddress = medicationMention.getAddress();
            int begin = medicationMention.getBegin();
            int end = medicationMention.getEnd();

            // Now we are going to get all the OntologyConcepts referenced by this MedicationMention
            FSArray ontologyConceptArr = medicationMention.getOntologyConceptArr();

            if (ontologyConceptArr != null) {
                // We have found some ontology concepts, so check to ensure they are UmlsConcepts and write them to
                // the CSV file

                // Convert the FSArray to an array of FeatureStructures so you can iterate over them
                FeatureStructure[] ontologyConceptFeatureStructures = ontologyConceptArr.toArray();
                Arrays.stream(ontologyConceptFeatureStructures).forEach(ontologyConceptFeatureStructure -> {
                    if (ontologyConceptFeatureStructure instanceof UmlsConcept) {
                        // This feature structure is a UmlsConcept, so cast it to one and extract its features
                        UmlsConcept umlsConcept = (UmlsConcept)ontologyConceptFeatureStructure;
                        int umlsConceptAddress = umlsConcept.getAddress();
                        String codingScheme = umlsConcept.getCodingScheme();
                        String code = umlsConcept.getCode();
                        String cui = umlsConcept.getCui();
                        String tui = umlsConcept.getTui();
                        String preferredText = umlsConcept.getPreferredText();

                        // Write the row to the writer. Note order is important here. This order must be the
                        // same as the headers
                        String[] row = {
                            String.valueOf(medicationMentionAddress),
                            String.valueOf(begin),
                            String.valueOf(end),
                            String.valueOf(umlsConceptAddress),
                            codingScheme,
                            code,
                            cui,
                            tui,
                            preferredText
                        };
                        _writer.writeNext(row, _doubleQuotes);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.info("Destroying");
        // Clean up writer. Note, this is a very important step. If you do not close the writer
        // then it may not write stuff still in the buffer. So make sure you flush and close it!
        try {
            _writer.flush();
            _writer.close();
        } catch (IOException e) {
            LOGGER.error("Error closing writer", e);
        }
    }
}
