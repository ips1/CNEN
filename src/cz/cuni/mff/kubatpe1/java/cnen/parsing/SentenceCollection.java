
package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import java.util.ArrayList;
import java.util.List;

/**
 * Class serving as a collection of sentence trees.
 * Simple wrapper for ArrayList.
 * @author Petr Kubat
 */
public class SentenceCollection {
    private List<SentenceTree> trees;
    
    /**
     * Default constructor for the SentenceCollection class.
     */
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
