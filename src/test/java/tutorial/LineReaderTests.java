package tutorial;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.util.Progress;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class LineReaderTests {
    public static final String DOCUMENT_FILE = "documents.txt";

    @Test
    public void readsOneDocumentPerLine() throws Exception {
        // Arrange: Setup our LineReader with documents.txt
        String inputFilePath = getClass().getClassLoader().getResource(DOCUMENT_FILE).getPath();
        CollectionReader collectionReader = CollectionReaderFactory.createReader(
            LineReader.class,
            LineReader.PARAM_INPUT_FILE, inputFilePath
        );

        // Act: Run the pipeline with just the CR (no AEs)
        AnalysisEngineDescription[] emptyAnalysisEngineArray = new AnalysisEngineDescription[]{};
        // Note: We don't even need a CAS to run a pipeline!
        SimplePipeline.runPipeline(collectionReader, emptyAnalysisEngineArray);

        // Assert: The LineReader CR read one document per line
        Progress[] progressArr = collectionReader.getProgress();
        Progress entitiesProgress = progressArr[0];
        int expectedNumberOfDocumentsProcessed = getNLinesInFile(inputFilePath);
        long actualNumberOfDocumentsProcessed = entitiesProgress.getCompleted();
        assertEquals(expectedNumberOfDocumentsProcessed, actualNumberOfDocumentsProcessed);
        assertEquals(entitiesProgress.getUnit(), Progress.ENTITIES);
    }

    private int getNLinesInFile(String path) throws Exception {
        int nLines = 0;
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        while (reader.readLine() != null) {
            ++nLines;
        }
        return nLines;
    }
}
