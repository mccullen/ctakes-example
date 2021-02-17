package tutorial;

import org.apache.ctakes.typesystem.type.structured.DocumentID;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * This is a collection reader so we are extending JCasCollectionReader_ImplBase
 * */
public class LineReader extends JCasCollectionReader_ImplBase {
    public static final Logger LOGGER = Logger.getLogger(LineReader.class.getName());

    // Use UIMA-FIT to specify ConfigurationParameters. You can set these values by name in the piper file.
    // If mandatory, the user must set it. Otherwise, the defaultValue will be used.
    public static final String PARAM_INPUT_FILE = "InputFile";
    @ConfigurationParameter(
        name = PARAM_INPUT_FILE,
        description = "The path to the input file containing the documents",
        mandatory = true,
        defaultValue = "*"
    )
    private String _inputFile;

    // The reader that we will use to read in files line by line
    private BufferedReader _reader;
    // The documentText for the current document. Set in hasNext()
    private String _documentText;
    // The number of documents processed so far
    private int _nDocumentsProcessed = 0;
    // The number of documents total. Used for implementing getProgress()
    private int _nDocumentsTotal = 0;

    /**
     * By the time you hit initialize, your configuration parameters WILL be set. So _inputFile WILL have
     * the value specified in the piper file. We will use this to set our reader so we can read the
     * input file in line by line.
     * */
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        LOGGER.info("Initializing line reader with this input file: " + _inputFile);
        try {
            FileInputStream fileInputStream = new FileInputStream(_inputFile);
            _reader = new BufferedReader(new InputStreamReader(fileInputStream));
            setNDocumentsTotal();
        } catch (IOException e) {
            LOGGER.error("Error loading file " + _inputFile, e);
        }
    }

    private void setNDocumentsTotal() {
        try {
            FileInputStream fileInputStream = new FileInputStream(_inputFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            while (reader.readLine() != null) {
                ++_nDocumentsTotal;
            }
        } catch (Exception e) {
            LOGGER.error("Error loading file " + _inputFile, e);
        }
    }

    /**
     * This gets called AFTER initialize() and BEFORE getNext(). It returns true if there are more documents to
     * read and false otherwise. If it is about to return false then you should cleanup your resources. There is
     * a close() and destroy() method but I'm not sure they ever actually get called.
     * */
    @Override
    public boolean hasNext() throws IOException, CollectionException {
        LOGGER.info("Checking for next document");
        // Each line in our _inputFile is a separate document
        _documentText = _reader.readLine();
        // If there are no lines left to read, then we are done and hasNext returns false.
        boolean result = _documentText != null;

        if (!result) {
            LOGGER.info("No more documents left, cleaning up now");
            // Clean up if no more documents left to process
            _reader.close();
        } else {
            LOGGER.info("Found more documents in file, continuing to process");
        }

        return result;
    }

    /**
     * This gets called AFTER hasNext() returns TRUE. The UIMA framework will provide you with the jCas and you
     * can do whatever you want with it here. In a CR, you mainly want to
     *  - set the document text
     *  - add a DocumentID annotation
     * */
    @Override
    public void getNext(JCas jCas) {
        ++_nDocumentsProcessed; // Increment number of documents we have processed so far
        LOGGER.info("Processing document number " + _nDocumentsProcessed);
        jCas.setDocumentText(_documentText);
        addDocumentIdAnnotation(jCas);
    }

    private void addDocumentIdAnnotation(JCas jCas) {
        // We are just going to use the current document number as the document Id

        // Instantiate the DocumentID type. This is one of the FeatureStructures defined in the cTAKES type system
        DocumentID documentId = new DocumentID(jCas);
        // Set its properties
        documentId.setDocumentID(String.valueOf(_nDocumentsProcessed));
        // DO NOT forget to add it to the indexes. This makes it retrievable from other UIMA components later in
        // the pipeline. I'm not sure if you would ever NOT want to do this
        documentId.addToIndexes();
    }

    // I usually just ignore this, but I've implemented it for illustration It is used to track the percent of
    // documents you have completed, probably used in UIMA tools that show a progress bar or something.
    // See https://uima.apache.org/d/uimaj-current/tutorials_and_users_guides.html#ugr.tug.cpe.collection_reader.developing
    @Override
    public Progress[] getProgress() {
        // This is how I would normally just ignore it
        //return new Progress[0];
        return new Progress[] {
            new ProgressImpl(_nDocumentsProcessed, _nDocumentsTotal, Progress.ENTITIES)
        };
    }

    // I'm not sure if this or destroy() actually ever get called. If I set a breakpoint and run, it never hits it.
    // So I just put all cleanup logic in hasNext() if it is about to right before it returns false.
    @Override
    public void close() throws IOException {
        super.close();
    }
}
