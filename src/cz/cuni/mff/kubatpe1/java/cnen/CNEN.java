/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.actions.SingleEntityNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.EntitySubtreeNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeAction;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeActionException;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedTextParser;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphoditaGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.PlaintextTreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceCollection;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.TreeTextMatcher;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.TextMatchingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the CNEN project, provides high-level normalization interface.
 * @author Petr
 */
public class CNEN {

    /**
     * Main method - runs all the normalization modes.
     * Mode is specified by a option passed as an argument
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               
        // Prepare encoding for input and output
        System.setProperty("file.encoding", "utf-8");
        PrintStream stdout;
        try {
            stdout = new PrintStream(System.out, true, "UTF-8");
        } 
        catch (UnsupportedEncodingException ex) {
            System.err.println("UTF-8 encoding must be supported!");
            System.exit(1);
            return;
        }
        
        if (args.length < 2) {
            System.err.println("Must have at least two arguments!");
            System.exit(1);
        }
        
        boolean fromText = args[0].equals("-t");
        
        if (fromText && args.length < 4) {
            System.err.println("Input  and output files must be specified!");
            System.exit(1);
        }
        
        // Temporary file for Treex input
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("CNEN", null);
            tmpFile.deleteOnExit();
        } 
        catch (IOException ex) {
            System.err.println("Can't create temporary file!");
            System.exit(1);
        }
        
        MorphologyGenerator generator;
        try {
            generator = new MorphoditaGenerator(fromText ? args[1] : args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
            System.exit(1);
            return;
        }
        
        
        TreeAction normalizer = fromText ? new EntitySubtreeNormalizer(false, generator) : new SingleEntityNormalizer(false, -1, generator);
        SentenceTreeParser parser = new PlaintextTreexParser();
        
        try {
            if (fromText) {
                // Normalizing in text;
                normalizeEntitiesInText(args[2], args[3], tmpFile.getAbsolutePath(), parser, normalizer);
                
            }
            else {
                // Normalizing single entity
                stdout.println(normalizeSingleEntity(args[1], tmpFile.getAbsolutePath(), parser, normalizer));
            }
        } 
        catch (NormalizationException ex) {
            System.err.println("Error during the normalization:");
            System.err.println(ex);
            System.exit(1);
        }
    }
    
    /**
     * Normalizes one entity using specified parser and normalizer.
     * @param input Input file containing single entity
     * @param tmpFileName Path to a temporary file to be used by the procedure
     * @param parser Parser used to get sentence trees from the text
     * @param normalizer Normalizer used to normalize the trees
     * @return String containing normalized named entity
     * @throws NormalizationException Normalization failed
     */
    public static String normalizeSingleEntity(String input, String tmpFileName, SentenceTreeParser parser, TreeAction normalizer) throws NormalizationException {
        // Read the entity
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            throw new NormalizationException("UTF-8 encoding must be supported!", ex);
        } catch (FileNotFoundException ex) {
            throw new NormalizationException("Input file doesn't exist!", ex);
        }
        StringBuilder entity = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {
                entity.append(line);
                entity.append("\n");
            }
        } catch (IOException ex) {
            throw new NormalizationException("Error reading input file!", ex);
        }
        
        // Store the entity into the temporary file
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(tmpFileName), true, "UTF8");
        } catch (FileNotFoundException ex) {
            throw new NormalizationException("Error using the temporary file!", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new NormalizationException("UTF-8 encoding must be supported!", ex);
        }
        out.print(entity.toString());
        out.close();
        
        // Parse sentence trees from the input file
        List<SentenceCollection> treeList;
        try {
            treeList = parser.parseDocumentSet(tmpFileName);
        } catch (TreeParsingException ex) {
            throw new NormalizationException("Error parsing the Treex output!", ex);
        }
        
        StringBuilder result = new StringBuilder();
        // Apply normalizer on each of the trees for each entity, collect the result on the way
        for (SentenceCollection col: treeList) {
            for (SentenceTree tree: col.getTrees()) {
                try {
                    normalizer.runOnTree(tree);
                } catch (TreeActionException ex) {
                    System.err.println("Error during the normalization: " + ex);
                }
                result.append(tree.toString());
                result.append(' ');
            }
            result.append('\n');
        }
        
        return result.toString();
    }  
    
    /**
     * Normalizes entities anotated in text with tags <ne>
     * @param input Input file containing entities
     * @param tmpFileName Path to a temporary file to be used by the procedure
     * @param parser Parser used to get sentence trees from the text
     * @param normalizer Normalizer used to normalize the trees
     * @return String containing the text with anotated entities with normalized forms in normalized_name attribute
     * @throws NormalizationException Normalization failed
     */
    public static void normalizeEntitiesInText(String input, String output, String tmpFileName, SentenceTreeParser parser, TreeAction normalizer) throws NormalizationException {
        // Fetch the anotated text and get anotations
        AnotatedText anotatedText = null;
        
        AnotatedTextParser textParser = new AnotatedTextParser(input, "ne");
        try {
            anotatedText = textParser.parseText(true);
        } catch (AnotationParsingException ex) {
            throw new NormalizationException("Error while parsing input file: \n" + ex, ex);
        }
                
        // Store the raw text into the temporary file
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(tmpFileName), true, "UTF8");
        } catch (FileNotFoundException ex) {
            throw new NormalizationException("Error using the temporary file!", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new NormalizationException("UTF-8 encoding must be supported!", ex);
        }
        out.print(anotatedText.getText());
        out.close();
        
        // Parse sentence trees from the input file
        SentenceCollection treeList;
        try {
            treeList = parser.parseDocument(tmpFileName);
        } catch (TreeParsingException ex) {
            throw new NormalizationException("Error parsing the Treex output!", ex);
        }
 
        // Matching the trees with anotations
        TreeTextMatcher matcher = new TreeTextMatcher(treeList);
        try {
            matcher.matchWithText(anotatedText);
        } catch (TextMatchingException ex) {
            throw new NormalizationException("Error while matching anotations with trees!", ex);
        }
                        
        for (SentenceTree tree: treeList.getTrees()) {
            try {
                normalizer.runOnTree(tree);
            } catch (TreeActionException ex) {
                System.err.println("Error during the normalization: " + ex);
            }
        }
        
        anotatedText.fetchNormalizedNames("normalized_name");
        try {
            anotatedText.generateNormalizedOutput(output);
        } catch (DOMException ex) {
            throw new NormalizationException("Result can't be written to a file!", ex);
        }
    }
}
