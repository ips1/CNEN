/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.actions.BasicRecursiveNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeAction;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeActionException;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.ManualGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.TreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
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
    public static void main(String[] args) throws TreeParsingException {
        // TODO code application logic here
        
        SentenceTreeParser tp = new TreexParser();
        
        SentenceTree tree = tp.parseTree("example_input.treex");
        
        TreeAction act = new BasicRecursiveNormalizer(true, new ManualGenerator());
        try {
            act.runOnTree(tree);
        } catch (TreeActionException ex) {
            System.err.println("TreeAction error!");
        }
        
        System.out.println(tree);
    }
    
}
