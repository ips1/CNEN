
package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;

/**
 * Interface for an action that can modify a SentenceTree.
 * Normalizers are typical examples of TreeActions.
 * @author Petr Kubat
 */
public interface TreeAction {
    /**
     * Performs a specific action on a sentence tree.
     * @param t Tree to be modified
     */
    public void runOnTree(SentenceTree t) throws TreeActionException;
}
