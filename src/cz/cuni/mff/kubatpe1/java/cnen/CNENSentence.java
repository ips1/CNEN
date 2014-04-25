/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.actions.EntitySubtreeNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeAction;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeActionException;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphoditaGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.TreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petrkubat
 */
public class CNENSentence {
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Input file, output file and treex scenario must be specified!");
            return;
        }
        
        String inFile = args[0];
        String outFile = args[1];
        String treexScenario = args[2];
        
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF8"));
        
        StringBuilder buffer = new StringBuilder();
        String line;
        
        while ((line = in.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }
        
        String tmpFileName = TreexInterface.removeSuffix(inFile) + ".tmp_treex_in";
        
        String result = null;
        
        try {
            result = normalizeSentences(buffer.toString(), tmpFileName, treexScenario);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TreeParsingException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TextMatchingException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TreeActionException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AnotationParsingException ex) {
            Logger.getLogger(CNENSentence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PrintStream out = new PrintStream(new FileOutputStream(outFile), true, "UTF8");

        out.print(result);
        
        out.close();
        
        File tmpFile = new File(tmpFileName);
        if (!tmpFile.delete()) {
            System.err.println("Temporary file " + tmpFileName + " can't be deleted.");
        }
        
    }    
    
    public static String normalizeSentences(String text, String tmpFileName, String scenario) throws UnsupportedEncodingException, FileNotFoundException, IOException, InterruptedException, TreeParsingException, TextMatchingException, TreeActionException, AnotationParsingException {
        AnotatedText anotatedText = AnotatedText.parseText(text, "ne");
        
        PrintStream out = new PrintStream(new FileOutputStream(tmpFileName), true, "UTF8");

        out.print(anotatedText.getText());
        
        out.close();
        
        String resultFile = TreexInterface.runTreex(tmpFileName, scenario, false);
        
        SentenceTreeParser parser = new TreexParser();
        
        List<SentenceTree> trees = parser.parseTree(resultFile);
        
        TreeTextMatcher matcher = new TreeTextMatcher(trees);
        
        matcher.matchWithText(anotatedText);
        
        TreeAction normalizer = new EntitySubtreeNormalizer(false, new MorphoditaGenerator("czech-morfflex-131112.dict"));
                
        for (SentenceTree tree: trees) {
            normalizer.runOnTree(tree);
        }
        
        anotatedText.fetchNormalizedNames();
        
        // Deleting the temporary treex result file
        File resFile = new File(resultFile);
        if (!resFile.delete()) {
            System.err.println("Temporary file " + tmpFileName + " can't be deleted.");
        }
        
        return anotatedText.generateNormalizedOutput("ne", "normalized_name");
    }
}
