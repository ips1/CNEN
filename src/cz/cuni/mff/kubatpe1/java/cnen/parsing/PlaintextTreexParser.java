/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.treex.TreexInterface;
import cz.cuni.mff.kubatpe1.java.cnen.treex.exceptions.TreexException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SentenceTreeParser implementation which creates sentence dependency trees directly from plaintext.
 * Wraps the TreexInterface and TreexParser.
 * Has a special method for parsing more documents from the same file.
 * @author petrkubat
 */
public class PlaintextTreexParser implements SentenceTreeParser {
    
    private final String treexScenario = "W2A::CS::Segment " +
                "W2A::CS::Tokenize " +
                "W2A::CS::TagFeaturama lemmatize=1 " +
                "W2A::CS::FixMorphoErrors " +
                "W2A::CS::ParseMSTAdapted " +
                "W2A::CS::FixAtreeAfterMcD " +
                "W2A::CS::FixIsMember " +
                "W2A::CS::FixPrepositionalCase " +
                "W2A::CS::FixReflexiveTantum " +
                "W2A::CS::FixReflexivePronouns";
    
    @Override
    public SentenceCollection parseDocument(String path) throws TreeParsingException {
        List<SentenceCollection> result = runTreexAndParse(path, false);
        
        if (result.size() != 1) throw new TreeParsingException("Too many documents");
        
        return result.get(0);
    }
    
    /**
     * Parses more SentenceCollections from one file (one per line).
     * @param path Path to source file
     * @return List of SentenceCollectons parsed
     * @throws TreeParsingException Parsing failed
     */
    public List<SentenceCollection> parseDocumentSet(String path) throws TreeParsingException {
        return runTreexAndParse(path, true);
    }
    
    private List<SentenceCollection> runTreexAndParse(String path, boolean moreDocs) throws TreeParsingException {
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
            sentences.add(parser.parseDocument(fileName));
            File tmpFile = new File(fileName);
            if (!tmpFile.delete()) {
                System.err.println("Temporary file " + fileName + " can't be deleted.");
            }
        }
        
        return sentences;
    }
}
