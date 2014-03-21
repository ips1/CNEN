/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.actions.BasicRecursiveNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeAction;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeActionException;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.ExternalProcessGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.ManualGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphoditaGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyLoadingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.TreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Petr
 */
public class CNEN {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TreeParsingException, MorphologyLoadingException, UnsupportedEncodingException {
        // TODO code application logic here
        System.setProperty("file.encoding", "utf-8");
        PrintStream out = new PrintStream(System.out, true, "UTF-8");


        
        
        if (args.length < 1) {
            System.err.println("Input file must be specified!");
            return;
        }
        

        out.println(normalize(args[0]));
    }
    
    public static String normalize(String inputFile) throws TreeParsingException {
        
        SentenceTreeParser tp = new TreexParser();
        
        SentenceTree tree = tp.parseTree(inputFile);
        
        /*
        String oldPath = System.getProperty("java.library.path");
        
        String newPath = "/home/ips/Bakalarka/morphodita-master/bindings/java" + ":" + oldPath;
        
        System.setProperty("java.library.path", newPath);  
        */
        
        //TreeAction act = new BasicRecursiveNormalizer(true, new MorphoditaGenerator("/home/ips/Bakalarka/morphodita-master/131112/czech-morfflex-131112-raw_lemmas.dict"));
        
        TreeAction act = new BasicRecursiveNormalizer(true, new MorphoditaGenerator("czech-morfflex-131112.dict"));

        
        try {
            act.runOnTree(tree);
        } catch (TreeActionException ex) {
            System.err.println("TreeAction error: " + ex.getMessage());
        }
        
        return tree.toString();
    }    
}
