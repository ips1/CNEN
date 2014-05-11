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
 * TreeAction implementation of a normalization procedure.
 * This class implements a rule procedure described in a bachelor thesis "Normalization of named entities in czech texts" by Petr Kubat"
 * 
 * @author Petr Kubat
 */
public class SingleEntityNormalizer implements TreeAction {

    private boolean toSingular;
    private int entityId;
    private MorphologyGenerator mg = null;
    private CaseMatcher caseMatcher;
    
    /**
     * Default constructor.
     * @param toSingular Boolean specifying whether the named entities should be turned to singular
     * @param entityId Identifier of an entity to be normalized (tree can contain more entities), -1 is a default value for nodes
     * @param mg Generator to be used for morphology generation
     */
    public SingleEntityNormalizer(boolean toSingular, int entityId, MorphologyGenerator mg) {
        this.entityId = entityId;
        this.toSingular = toSingular;
        this.mg = mg;
        this.caseMatcher = new CaseMatcher();
    }
    
    
    
    /**
     * Performs the normalization algorithm on a tree
     * @param t Tree to be modified
     */
    @Override
    public void runOnTree(SentenceTree t) throws TreeActionException {
       
        TreeNode root = t.getRoot();
        
        if (root != null) {
            root.setNormalizedInEntity(entityId, true);

            for (TreeNode n: root.getChildren()) {
                rootAction(n);
            }        
        }      
    }
    
    /**
     * Performs the normalization beginning in a subtree
     * @param t Root of a subtree to be modified
     * @throws TreeActionException 
     */
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
        if (t.grCase != '1' && t.grCase != 'X' && t.grCase != '-') {
            normalized = false;
        }
        if (toSingular && !t.isSingular()) {
            normalized = false;
        }
        return normalized;
    }
    
    /**
     * Marks whole subtree of a specified node (including it) as normalized
     * @param node Node whose subtree is to be marked
     */
    private void markSubtreeAsNormalized(TreeNode node) {
        if (entityId != -1 && !node.isInEntity(entityId)) return;
        
        node.setNormalizedInEntity(entityId, true);
        
        for (TreeNode child: node.getChildren()) {
            markSubtreeAsNormalized(child);
        }
    }
    
    /**
     * Skips the node n as a conjunction, applies specified action on direct children, solves the rest.
     * @param n Conjunction node 
     * @param parent Parent node of conjunction node, if null, conjunction node is root
     * @param actionType Type of action to perform on nodes connected
     */
    private void skipConjunction(TreeNode node, TreeNode parent, RecursiveActionType actionType) throws TreeActionException {
        if (node.isInEntity(entityId)) {
            node.setNormalizedInEntity(entityId, true);
        }
        
        // First look if there is a coordination
        boolean hasCoordination = false;
        TreeNode coordinationNode = null;
        
        for (TreeNode n: node.getChildren()) {
            if (n.getAfun().isCoordination()) {
                hasCoordination = true;
                coordinationNode = n;
                break;
            }
        }

        if (hasCoordination) {
            // There is a coordination for the conjunction
            for (TreeNode n: node.getChildren()) {
                if (n.getAfun().isCoordination()) {
                    // All nodes in coordination will be the children of the conjunction's parent
                    switch (actionType) {
                        case LEFT: leftChildAction(n, parent); break;
                        case RIGHT: rightChildAction(n, parent); break;
                        case ROOT: rootAction(n); break;
                    }
                }
                else {
                    // All other nodes will be children of the first node in concordance
                    if (n.getOrder() < node.getOrder()) {
                        leftChildAction(n, coordinationNode);
                    }
                    else {
                        rightChildAction(n, coordinationNode);
                    }
                }
            }            
        }
        else {
            // There is no coordination
            // All nodes will be children of the conjunction's parent
            for (TreeNode n: node.getChildren()) {
                switch (actionType) {
                    case LEFT: leftChildAction(n, parent); break;
                    case RIGHT: rightChildAction(n, parent); break;
                    case ROOT: rootAction(n); break;
                }
            }
        }
        
    }
    
    /**
     * Performs recursive step for a root node.
     * @param root Root node
     * @throws TreeActionException  
     */
    private void rootAction(TreeNode root) throws TreeActionException {
        if (entityId != -1 && !root.isInEntity(entityId)) return;
        
        root.setNormalizedInEntity(entityId, true);
        
        // Conjunctions and punctuation have to be skipped
        if (root.getTagInEntity(entityId).isConjunction() || root.getTagInEntity(entityId).isPunctuation()) {
            skipConjunction(root, null, RecursiveActionType.ROOT);
            return;
        }        
                
        // If the root is already normalized, we do nothing
        // The same goes for the verbs in root
        // And for prepositions in root
        if (isNormalizedTag(root.getTagInEntity(entityId)) || root.getTagInEntity(entityId).isVerb() || root.getTagInEntity(entityId).isPreposition()) {
            // We have to mark all descendants as normalized
            markSubtreeAsNormalized(root);
            return;
        }
        
        normalizeTag(root.getTagInEntity(entityId));
        
        String oldWord = root.getContentInEntity(entityId);
        String newWord = root.getContentInEntity(entityId);
        
        boolean success = false;
        try {
            newWord = mg.generateForTag(root.getLemmaInEntity(entityId), root.getTagInEntity(entityId));
            success = true;
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        // If we try to generate for X and fail, we try to swap for S
        if (!success && root.getTagInEntity(entityId).number == 'X') {
            root.getTagInEntity(entityId).number = 'S';
            try {
                newWord = mg.generateForTag(root.getLemmaInEntity(entityId), root.getTagInEntity(entityId));
            }
            catch (MorphologyGeneratingException ex) {
                System.err.println(ex.getMessage());
            }
        }

        
        root.setContentInEntity(entityId, caseMatcher.matchCase(oldWord, newWord));
        
        applyOnChildren(root);
    }
    
    private void leftChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        // Conjunctions and punctuation have to be skipped
        if (child.getTagInEntity(entityId).isConjunction() || child.getTagInEntity(entityId).isPunctuation()) {
            skipConjunction(child, parent, RecursiveActionType.LEFT);
            return;
        }
        
        // We got out of the entity, stoping normalization
        if (entityId != -1 && !child.isInEntity(entityId)) return;
        
        child.setNormalizedInEntity(entityId, true);
        
        Tag parentTag = parent.getTagInEntity(entityId);
        Tag childTag = child.getTagInEntity(entityId);
        
        childTag.matchWithTag(parentTag);
        
        String oldWord = child.getContentInEntity(entityId);
        String newWord = child.getContentInEntity(entityId);
        try {
            newWord = mg.generateForTag(child.getLemmaInEntity(entityId), childTag);
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        child.setContentInEntity(entityId, caseMatcher.matchCase(oldWord, newWord));
        
        applyOnChildren(child);
    }
    
    private void rightChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        // Conjunctions and punctuation have to be skipped
        if (child.getTagInEntity(entityId).isConjunction() || child.getTagInEntity(entityId).isPunctuation()) {
            skipConjunction(child, parent, RecursiveActionType.RIGHT);
            return;
        }
        
        // We got out of the entity, stoping normalization
        if (entityId != -1 && !child.isInEntity(entityId)) return;
        
        // Attributes standing on the right side of the parent have to be reevaluated
        if (child.getAfun().isAttribute()) {
            Tag childTag = child.getTagInEntity(entityId);
            if (childTag.isNoun() || childTag.isAdjective() || childTag.isPronoun() || childTag.isNumeral()) {
                // For nouns, adjectives, numerals or pronouns we check if there was match previously
                // Additionally, we check if the child was immediately preceeded by the parent
                if (childTag.matchesGrCase(parent.getOriginalTag()) 
                        && childTag.matchesGender(parent.getOriginalTag()) 
                        && childTag.matchesNumber(parent.getOriginalTag())
                        && (child.getOrder() - parent.getOrder() == 1)) {
                    leftChildAction(child, parent);
                }
            }
        }

        /*
        if (child.getTagInEntity(entityId).isAdjective() && parent.getTagInEntity(entityId).isNoun()) {
            leftChildAction(child, parent);
            return;
        }
        */
        
        child.setNormalizedInEntity(entityId, true);
        
        // Conjunctions and punctuation have to be skipped
        if (child.getTagInEntity(entityId).isConjunction() || child.getTagInEntity(entityId).isPunctuation()) {
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
}
