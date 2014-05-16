

package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * TreeAction implementing normalization procedure on a complex sentence tree
 * where only several subtrees represent named entities.
 * Normalizes all the entities in the sentence tree using SingleEntityNormalizer.
 * The SentenceTree has to be previously matched with AnotatedText.
 * @author Petr Kubat
 */
public class EntitySubtreeNormalizer implements TreeAction {

    // Change the number of entities to singular
    private boolean toSingular;
    // MorphologyGenerator used to generate new forms
    private MorphologyGenerator mg;

    /**
     * Default constructor for EntitySubtreeNormalizer class.
     * @param toSingular Change the number of entities to singular.
     * @param mg MorphologyGenerator used to generate new forms.
     */
    public EntitySubtreeNormalizer(boolean toSingular, MorphologyGenerator mg) {
        this.mg = mg;
        this.toSingular = toSingular;
    }
    
    /**
     * Performs the normalization on a tree.
     * @param t Tree to be normalized.
     * @throws TreeActionException Normalization failed.
     */
    @Override
    public void runOnTree(SentenceTree t) throws TreeActionException {
        // Stack for DFS of the tree
        Stack<TreeNode> stack = new Stack<TreeNode>();
        
        stack.push(t.getRoot());
        
        // Performing DFS
        while (stack.size() > 0) {
            TreeNode currentNode = stack.pop();
            
            List<Integer> entityIds = currentNode.getEntityIds();
            for (Integer i: entityIds) {
                if (!currentNode.isNormalizedInEntity(i)) {
                    // For each node representing a not-yet normalized entity
                    // we run the SingleEntityNormalizer on the subtree
                    // specifically to normalize the entity
                    System.err.println("Starting normalization of " + i + " on " + currentNode.getContentInEntity(-1));
                    SingleEntityNormalizer normalizer = new SingleEntityNormalizer(toSingular, i, mg);
                    normalizer.normalizeSubtree(currentNode);
                }
            }
            for (TreeNode childNode: currentNode.getChildren()) {
                stack.push(childNode);
            }
           
        }
    }
    
}
