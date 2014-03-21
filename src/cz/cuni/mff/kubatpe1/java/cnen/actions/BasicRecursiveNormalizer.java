/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyGeneratingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Petr
 */
public class BasicRecursiveNormalizer implements TreeAction {

    private boolean singular;
    private MorphologyGenerator mg = null;
    private CaseMatcher caseMatcher;

    public BasicRecursiveNormalizer(boolean singular, MorphologyGenerator mg) {
        this.singular = singular;
        this.mg = mg;
        this.caseMatcher = new CaseMatcher();
    }
    
    
    
    /**
     * Performs basic recursive normalization on a tree
     * @param t Tree to be modified
     */
    @Override
    public void runOnTree(SentenceTree t) throws TreeActionException {
       
        TreeNode root = t.getRoot();
        
        for (TreeNode n: root.getChildren()) {
            rootAction(n);
        }        
    }
    
    /**
     * Normalizes the tag (puts it to first case and singular / plural)
     * @param t Tag to be normalized
     */
    private void normalizeTag(Tag t) {
        t.grCase = '1';
        if (singular) {
            t.number = 'S';
        }
        else {
            t.number = 'P';
        }
    } 
    
    private void rootAction(TreeNode root) throws TreeActionException {
        // Conjunctions have to be skipped
        if (root.getTag().wordClass == 'J') {
            for (TreeNode n: root.getChildren()) {
                rootAction(n);
            }   
            return;
        }
        
        normalizeTag(root.getTag());
        
        String oldWord = root.getContent();
        String newWord = root.getContent();
        try {
            newWord = mg.generateForTag(root.getLemma(), root.getTag());
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        root.setContent(caseMatcher.matchCase(oldWord, newWord));
        
        applyOnChildren(root);
    }
    
    private void leftChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        // Conjunctions have to be skipped
        if (child.getTag().wordClass == 'J') {
            for (TreeNode n: child.getChildren()) {
                leftChildAction(n, parent);
            }
            return;
        }
        
        Tag parentTag = parent.getTag();
        Tag childTag = child.getTag();
        
        childTag.grCase = parentTag.grCase;
        childTag.number = parentTag.number;
        
        String oldWord = child.getContent();
        String newWord = child.getContent();
        try {
            newWord = mg.generateForTag(child.getLemma(), childTag);
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        child.setContent(caseMatcher.matchCase(oldWord, newWord));
        
        applyOnChildren(child);
    }
    
    private void rightChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        applyOnChildren(child);
    }

    /**
     * Applies the recursive procedure on all children.
     * Recursive call depends on position of the child
     * @param n Node to be used
     */
    private void applyOnChildren(TreeNode n) throws TreeActionException {
        for (TreeNode child: n.getChildren()) {
            if (child.getOrder() < n.getOrder()) {
                leftChildAction(child, n);
            }
            else if (child.getOrder() > n.getOrder()) {
                rightChildAction(child, n);
            }
            else {
                throw new TreeActionException("Two nodes with same order");
            }
        }
    }
    
}
