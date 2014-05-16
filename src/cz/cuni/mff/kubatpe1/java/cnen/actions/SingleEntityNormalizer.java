
package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyGeneratingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TreeAction implementing a normalization procedure of a tree (or subtree) 
 * consisting of one single entity only.
 * This class implements a rule procedure described in a bachelor thesis 
 * "Normalization of named entities in Czech texts" by Petr Kubat.
 * It can be used to normalize subtrees of other trees as well, the subtree
 * is limited to the nodes with provided entity id.
 * @author Petr Kubat
 */
public class SingleEntityNormalizer implements TreeAction {

    // Change the number of entity to singular
    private boolean toSingular;
    // ID of the entity which is normalized (limits the part of the tree which is normalized)
    private int entityId;
    // MorphologyGenerator used to generate new forms
    private MorphologyGenerator mg = null;
    // CaseMatcher used by the normalizer
    private CaseMatcher caseMatcher;
    
    /**
     * Default constructor for the SingleEntityNormalizer class.
     * @param toSingular Change the number of entity to singular.
     * @param entityId Identifier of an entity to be normalized (tree can 
     * contain more entities), -1 is a default value for nodes (meaning that 
     * the whole tree is normalized).
     * @param mg MorphologyGenerator used to generate new forms.
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
     * @throws TreeActionException Normalization failed.
     */
    @Override
    public void runOnTree(SentenceTree t) throws TreeActionException {
       
        // Skips the real root (not containing any token) and calls rootAction
        // on all its children
        
        TreeNode root = t.getRoot();
        
        if (root != null) {
            root.setNormalizedInEntity(entityId, true);

            for (TreeNode n: root.getChildren()) {
                rootAction(n);
            }        
        }      
    }
    
    /**
     * Performs the normalization beginning in a subtree.
     * Doesn't skip the root.
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
     * Marks whole subtree of a specified node (including it) as normalized.
     * Is limited on the nodes with the same entityId.
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
     * Skips the node n as a conjunction, applies specified action on direct 
     * children, solves the rest.
     * Direct children are treated as children of the parent node.
     * Other children are treated as children of one of the direct children.
     * @param n Conjunction node.
     * @param parent Parent node of conjunction node, if null, conjunction node
     * is root.
     * @param actionType Type of action to perform on direct children.
     */
    private void skipConjunction(TreeNode node, TreeNode parent, RecursiveActionType actionType) throws TreeActionException {
        // Set the conjunction as normalized
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
                    // All other nodes will be children of the first node in coordination
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
     * Root node is a node containing a token, not an artificial root node.
     * @param root Root node.
     * @throws TreeActionException Action failed.
     */
    private void rootAction(TreeNode root) throws TreeActionException {
        // First check if the node is in required entity
        if (entityId != -1 && !root.isInEntity(entityId)) return;
        
        // Mark node as normalized
        root.setNormalizedInEntity(entityId, true);
        
        // Conjunctions and punctuation have to be skipped
        if (root.getTagInEntity(entityId).isConjunction() || root.getTagInEntity(entityId).isPunctuation()) {
            skipConjunction(root, null, RecursiveActionType.ROOT);
            return;
        }        
                
        // If the root is already normalized, we do not normalize
        // The same goes for the verbs in root
        // And for prepositions in root
        if (isNormalizedTag(root.getTagInEntity(entityId)) 
                || root.getTagInEntity(entityId).isVerb() 
                || root.getTagInEntity(entityId).isPreposition()) {
            // We have to mark all descendants as normalized
            markSubtreeAsNormalized(root);
            return;
        }
        
        // Normalize the root tag
        normalizeTag(root.getTagInEntity(entityId));
        
        String oldWord = root.getContentInEntity(entityId);
        String newWord = root.getContentInEntity(entityId);
        
        // Try to generate normalized form
        boolean success = false;
        try {
            newWord = mg.generateForTag(root.getLemmaInEntity(entityId), root.getTagInEntity(entityId));
            success = true;
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        // If we try to generate for number X and fail, we try to swap for S
        if (!success && root.getTagInEntity(entityId).number == 'X') {
            root.getTagInEntity(entityId).number = 'S';
            try {
                newWord = mg.generateForTag(root.getLemmaInEntity(entityId), root.getTagInEntity(entityId));
            }
            catch (MorphologyGeneratingException ex) {
                System.err.println(ex.getMessage());
            }
        }

        // Set new content
        root.setContentInEntity(entityId, caseMatcher.matchCase(oldWord, newWord));
        
        // Continue the recursive procedure
        applyOnChildren(root);
    }
    
    /**
     * Recursive step for a node which is a left child of its parent.
     * Used for right children that are to be treated as left children as well.
     * Left children are matched with their parents, right are not.
     * @param child Node on which the step is performed.
     * @param parent Parent node.
     * @throws TreeActionException Action failed.
     */
    private void leftChildAction(TreeNode child, TreeNode parent) throws TreeActionException {
        // Conjunctions and punctuation have to be skipped
        if (child.getTagInEntity(entityId).isConjunction() || child.getTagInEntity(entityId).isPunctuation()) {
            skipConjunction(child, parent, RecursiveActionType.LEFT);
            return;
        }
        
        // We got out of the entity, stoping normalization
        if (entityId != -1 && !child.isInEntity(entityId)) return;
        
        // Marking as normalized
        child.setNormalizedInEntity(entityId, true);
        
        // Left child always has to be matched with parent
        // Performing match with parent form
        Tag parentTag = parent.getTagInEntity(entityId);
        Tag childTag = child.getTagInEntity(entityId);
        
        childTag.matchWithTag(parentTag);
        
        String oldWord = child.getContentInEntity(entityId);
        String newWord = child.getContentInEntity(entityId);
        // Try to generate matched form
        try {
            newWord = mg.generateForTag(child.getLemmaInEntity(entityId), childTag);
        } catch (MorphologyGeneratingException ex) {
            System.err.println(ex.getMessage());
        }
        
        // Set new content
        child.setContentInEntity(entityId, caseMatcher.matchCase(oldWord, newWord));
        
        // Continue the recursive procedure
        applyOnChildren(child);
    }

    /**
     * Recursive step for a node which is a right child of its parent.
     * Left children not matched with their parents.
     * Have to first check whether to treat the node as left child instead
     * (special types of attributes in Czech language).
     * @param child Node on which the step is performed.
     * @param parent Parent node.
     * @throws TreeActionException Action failed.
     */
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
        
        // Set new content
        child.setNormalizedInEntity(entityId, true);
        
        // Continue the recursive procedure
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
