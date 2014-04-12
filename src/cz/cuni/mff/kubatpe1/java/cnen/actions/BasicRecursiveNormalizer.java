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

    private boolean toSingular;
    private int entityId;
    private MorphologyGenerator mg = null;
    private CaseMatcher caseMatcher;

    public BasicRecursiveNormalizer(boolean toSingular, int entityId, MorphologyGenerator mg) {
        this.entityId = entityId;
        this.toSingular = toSingular;
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
        
        root.setNormalized(true);
        
        for (TreeNode n: root.getChildren()) {
            rootAction(n);
        }        
    }
    
    public void normalizeSubtree(TreeNode t) throws TreeActionException {
        rootAction(t);
    }
    
    /**
     * Normalizes the tag (puts it to first case and singular / plural)
     * @param t Tag to be normalized
     */
    private void normalizeTag(Tag t) {
        t.grCase = '1';
        if (toSingular) {
            t.number = 'S';
        }
    } 
    
    /**
     * Finds out whether the specified tag is in normalized form.
     * @param t Tag to be checked
     * @return True if the tag is in 1st case (and singular if required)
     */
    private boolean isNormalizedTag(Tag t) {
        boolean normalized = true;
        if (t.grCase != '1') {
            normalized = false;
        }
        if (toSingular && !t.isSingular()) {
            normalized = false;
        }
        return normalized;
    }
    
    private void rootAction(TreeNode root) throws TreeActionException {
        if (entityId != -1 && root.getEntityId() != entityId) return;
        
        root.setNormalized(true);
        
        // Conjunctions and punctuation have to be skipped
        if (root.getTag().isConjunction() || root.getTag().isPunctuation()) {
            for (TreeNode n: root.getChildren()) {
                rootAction(n);
            }   
            return;
        }        
        
        // Trees with verb root are not normalized
        if (root.getTag().isVerb()) {
            return;
        }
        
        // If the root is already normalized, we do nothing
        if (isNormalizedTag(root.getTag())) {
            return;
        }
        
        normalizeTag(root.getTag());
        
        String oldWord = root.getContent();
        String newWord = root.getContent();
        
        boolean success = false;
        try {
            newWord = mg.generateForTag(root.getLemma(), root.getTag());
            success = true;
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        // If we try to generate for X and fail, we try to swap for S
        if (!success && root.getTag().number == 'X') {
            root.getTag().number = 'S';
            try {
                newWord = mg.generateForTag(root.getLemma(), root.getTag());
            }
            catch (MorphologyGeneratingException ex) {
                System.err.println(ex.getMessage());
            }
        }

        
        root.setContent(caseMatcher.matchCase(oldWord, newWord));
        
        applyOnChildren(root);
    }
    
    private void leftChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        if (entityId != -1 && child.getEntityId() != entityId) return;
        
        child.setNormalized(true);
        
        // Conjunctions and punctuation have to be skipped
        if (child.getTag().isConjunction() || child.getTag().isPunctuation()) {
            for (TreeNode n: child.getChildren()) {
                leftChildAction(n, parent);
            }
            return;
        }
        
        Tag parentTag = parent.getTag();
        Tag childTag = child.getTag();
        
        matchTags(parentTag, childTag);
        
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
        if (entityId != -1 && child.getEntityId() != entityId) return;
        
        child.setNormalized(true);
        
        // Conjunctions and punctuation have to be skipped
        if (child.getTag().isConjunction() || child.getTag().isPunctuation()) {
            for (TreeNode n: child.getChildren()) {
                rightChildAction(n, parent);
            }
            return;
        } 
        
        
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
    
    /**
     * Matches two tags in matter of gramatical number, case and gender.
     * @param source Source of the number, case and gender
     * @param target Tag to be matched
     */
    private void matchTags(Tag source, Tag target) {
        if (source.grCase != 'X' && source.grCase != '-') {
            target.grCase = source.grCase;
        }
        if (source.number != 'X' && source.number != '-') {
            target.number = source.number;        
        }
        
        // For adjectives, we match the gender as well
        if (target.isAdjective()) {
            if (source.gender != 'X' && source.gender != '-') {
                target.gender = source.gender;
            }
        }
    }
}
