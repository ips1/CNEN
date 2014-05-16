
package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.*;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.FileInputStream;
import java.util.List;


/**
 * Class for parsing SentenceTrees from external files.
 * @author Petr Kubat
 */
public interface SentenceTreeParser {
    /**
     * Parses one SentenceCollection from one file.
     * The collection represents one document, split into sentences.
     * @param path Path to source file.
     * @return SentenceCollection containing the SentenceTrees.
     * @throws TreeParsingException Parsing failed.
     */
    SentenceCollection parseDocument(String path) throws TreeParsingException;
    
    /**
     * Parses more SentenceCollections from one file.
     * Method of separation is defined by the implementation.
     * @param path Path to source file.
     * @return List of SentenceCollectons parsed.
     * @throws TreeParsingException Parsing failed.
     */
    List<SentenceCollection> parseDocumentSet(String path) throws TreeParsingException;
}
