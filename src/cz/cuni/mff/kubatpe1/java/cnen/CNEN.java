/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceTreeParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.TreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;

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
        
        System.out.println("Hi der!");
        
        System.out.println(tree);
    }
    
}
