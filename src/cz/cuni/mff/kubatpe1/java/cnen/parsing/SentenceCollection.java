/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import java.util.ArrayList;
import java.util.List;

/**
 * Class serving as a collection of sentence trees.
 * @author petrkubat
 */
public class SentenceCollection {
    private List<SentenceTree> trees;
    
    public SentenceCollection() {
        trees = new ArrayList<SentenceTree>();
    }
    
    /**
     * Appends a new tree to the end of the document.
     * @param tree New tree to be added
     */
    public void addTree(SentenceTree tree) {
        trees.add(tree);
    }
    
    /**
     * Retrieves all the trees in a document in an form of an iterable list.
     * @return List of all trees in a document
     */
    public List<SentenceTree> getTrees() {
        return trees;
    }
}
