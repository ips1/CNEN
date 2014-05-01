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
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphoditaGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.PlaintextTreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceCollection;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeTextMatcher;
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
            return;
        }
        
        if (args.length < 2) {
            System.err.println("Must have at least two arguments!");
            return;
        }
        
        boolean fromText = args[0].equals("-t");
        
        if (fromText && args.length < 4) {
            System.err.println("Input and output files must be specified!");
            return;
        }
        
        // Temporary file for Treex input
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("CNEN", null);
            tmpFile.deleteOnExit();
        } 
        catch (IOException ex) {
            System.err.println("Can't create temporary file!");
            return;
        }
        
        MorphologyGenerator generator = new MorphoditaGenerator(fromText ? args[1] : args[0]);
        
        
        TreeAction normalizer = fromText ? new EntitySubtreeNormalizer(false, generator) : new SingleEntityNormalizer(false, -1, generator);
        SentenceTreeParser parser = new PlaintextTreexParser();
        
        try {
            if (fromText) {
                // Reading input file
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[2]), "UTF8"));
        
                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }
                
                in.close();
                
                // Performing normalization
                
                String result = normalizeEntitiesInText(buffer.toString(), tmpFile.getAbsolutePath(), parser, normalizer);

                // Writing output file
                PrintStream out = new PrintStream(new FileOutputStream(args[3]), true, "UTF8");
                out.print(result);
                out.close();
            }
            else {
                // Normalizing single entity
                stdout.println(normalizeSingleEntity(args[1], tmpFile.getAbsolutePath(), parser, normalizer));
            }
        } 
        catch (NormalizationException ex) {
            System.err.println("Error during the normalization:");
            System.err.println(ex);
            return;
        } 
        catch (UnsupportedEncodingException ex) {
            System.err.println("UTF-8 encoding must be supported!");
            return;
        } 
        catch (FileNotFoundException ex) {
            System.err.println("Input file doesn't exist!");
            return;
        } catch (IOException ex) {
            System.err.println("Error while reading or writing the file!");
            return;
        }
    }
    
    /**
     * Normalizes one entity using specified parser and normalizer.
     * @param input String containing the single entity
     * @param tmpFileName Path to a temporary file to be used by the procedure
     * @param parser Parser used to get sentence trees from the text
     * @param normalizer Normalizer used to normalize the trees
     * @return String containing normalized named entity
     * @throws NormalizationException Normalization failed
     */
    public static String normalizeSingleEntity(String input, String tmpFileName, SentenceTreeParser parser, TreeAction normalizer) throws NormalizationException {
        // Store the entity into the temporary file
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(tmpFileName), true, "UTF8");
        } catch (FileNotFoundException ex) {
            throw new NormalizationException("Error using the temporary file!", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new NormalizationException("UTF-8 encoding must be supported!", ex);
        }
        out.print(input);
        out.close();
        
        // Parse sentence trees from the input file
        SentenceCollection treeList;
        try {
            treeList = parser.parseDocument(tmpFileName);
        } catch (TreeParsingException ex) {
            throw new NormalizationException("Error parsing the Treex output!", ex);
        }
        
        // Apply normalizer on each of the trees, collect the result on the way
        StringBuilder result = new StringBuilder();
        for (SentenceTree tree: treeList.getTrees()) {
            try {
                normalizer.runOnTree(tree);
            } catch (TreeActionException ex) {
                System.err.println("Error during the normalization: " + ex.getMessage());
            }
            result.append(tree.toString());
            result.append(' ');
        }
        
        return result.toString();
    }  
    
    /**
     * Normalizes entities anotated in text with tags <ne>
     * @param text Input text containing entities
     * @param tmpFileName Path to a temporary file to be used by the procedure
     * @param parser Parser used to get sentence trees from the text
     * @param normalizer Normalizer used to normalize the trees
     * @return String containing the text with anotated entities with normalized forms in normalized_name attribute
     * @throws NormalizationException Normalization failed
     */
    public static String normalizeEntitiesInText(String text, String tmpFileName, SentenceTreeParser parser, TreeAction normalizer) throws NormalizationException {
        // Fetch the anotated text and get anotations
        AnotatedText anotatedText = null;
        try {
            anotatedText = AnotatedText.parseText(text, "ne");
        } catch (AnotationParsingException ex) {
            throw new NormalizationException("Invalid input file format - anotations can't be parsed!", ex);
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
                System.err.println("Error during the normalization: " + ex.getMessage());
            }
        }
        
        anotatedText.fetchNormalizedNames();
        
        return anotatedText.generateNormalizedOutput("ne", "normalized_name");
    }
}
