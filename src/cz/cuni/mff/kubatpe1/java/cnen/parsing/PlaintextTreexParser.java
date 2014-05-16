
package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.treex.TreexInterface;
import cz.cuni.mff.kubatpe1.java.cnen.treex.exceptions.TreexException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * SentenceTreeParser implementation which creates sentence dependency trees directly from plaintext.
 * Wraps the TreexInterface and TreexParser.
 * Has a special method for parsing more documents from the same file.
 * @author Petr Kubat
 */
public class PlaintextTreexParser implements SentenceTreeParser {
       
    // Default scenario, is used when the scenario file is not found
    private final String defaultScenario = "W2A::CS::Segment " +
                "W2A::CS::Tokenize " +
                "W2A::CS::TagFeaturama lemmatize=1 " +
                "W2A::CS::FixMorphoErrors " +
                "W2A::CS::ParseMSTAdapted " +
                "W2A::CS::FixAtreeAfterMcD " +
                "W2A::CS::FixIsMember " +
                "W2A::CS::FixPrepositionalCase " +
                "W2A::CS::FixReflexiveTantum " +
                "W2A::CS::FixReflexivePronouns";
    
    // Default scenario file path
    private final String scenarioPath = "treex_scen.txt";
    
    /**
     * Runs Treex on the input plaintext and fetches result as one document.
     * @param path Path to the source file.
     * @return SentenceCollection containing the SentenceTrees.
     * @throws TreeParsingException Treex run or parsing failed.
     */
    @Override
    public SentenceCollection parseDocument(String path) throws TreeParsingException {
        List<SentenceCollection> result = runTreexAndParse(path, false);
        
        if (result.size() != 1) throw new TreeParsingException("Too many documents");
        
        return result.get(0);
    }
    
    /**
     * Runs Treex on the input plaintext and fetches result as set of documents
     * (one per line). 
     * @param path Path to the source file.
     * @return List of SentenceCollection, each for one parsed line.
     * @throws TreeParsingException Treex run or parsing failed.
     */
    @Override
    public List<SentenceCollection> parseDocumentSet(String path) throws TreeParsingException {
        return runTreexAndParse(path, true);
    }
    
    private List<SentenceCollection> runTreexAndParse(String path, boolean moreDocs) throws TreeParsingException {
        // Runs the Treex and then uses TreexParser to get SentenceTrees from the result
        
        // Fetch the scenario from the file
        BufferedReader in;
        String treexScenario;
        try {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(scenarioPath)));
       
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            
            treexScenario = result.toString();
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
            treexScenario = defaultScenario;
        }
        
        // Run the treex
        List<String> fileNames = null;
        try {
            fileNames = TreexInterface.runTreex(path, treexScenario, moreDocs);
        } catch (TreexException ex) {
            throw new TreeParsingException("Error running treex", ex);
        }
        
        if (fileNames == null) throw new TreeParsingException("Treex returned no result!");
                
        // Parse resulting treex files
        TreexParser parser = new TreexParser();
        
        List<SentenceCollection> sentences = new ArrayList<SentenceCollection>();
    
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            System.err.println("Parsing file " + fileName);
            sentences.add(parser.parseDocument(fileName));
            File tmpFile = new File(fileName);
            if (!tmpFile.delete()) {
                System.err.println("Temporary file " + fileName + " can't be deleted.");
            }
        }
        
        return sentences;
    }
}
