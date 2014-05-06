/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.actions;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphologyGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author petrkubat
 */
public class EntitySubtreeNormalizer implements TreeAction {

    private boolean toSingular;
    private MorphologyGenerator mg;

    public EntitySubtreeNormalizer(boolean toSingular, MorphologyGenerator mg) {
        this.mg = mg;
        this.toSingular = toSingular;
    }
    
    @Override
    public void runOnTree(SentenceTree t) throws TreeActionException {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        
        stack.push(t.getRoot());
        
        while (stack.size() > 0) {
            TreeNode currentNode = stack.pop();
            List<Integer> entityIds = currentNode.getEntityIds();
            for (Integer i: entityIds) {
                if (!currentNode.isNormalizedInEntity(i)) {
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
