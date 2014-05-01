/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;

/**
 * Interface for an action that can modify a SentenceTree.
 * @author Petr Kubat
 */
public interface TreeAction {
    /**
     * Performs a specific action on a sentence tree.
     * @param t Tree to be modified
     */
    public void runOnTree(SentenceTree t) throws TreeActionException;
}
