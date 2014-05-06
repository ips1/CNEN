/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.anotations;

import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.EntityAnotation;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceCollection;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.TextMatchingException;
import java.util.List;

/**
 *
 * @author petrkubat
 */
public class TreeTextMatcher {

    private SentenceCollection treeSet;
    
    public TreeTextMatcher(SentenceCollection treeSet) {
        this.treeSet = treeSet;
    }
    
    public TreeTextMatcher() {
        treeSet = new SentenceCollection();
    }
    
    public void clearTreeSet() {
        treeSet = new SentenceCollection();
    }
    
    public void addTree (SentenceTree tree) {
        treeSet.addTree(tree);
    }
    
    public void matchWithText(AnotatedText text) throws TextMatchingException {
        // Assuming there is unlimited white space between tokens
        
        int currentPosition = 0;
        for (SentenceTree currentTree: treeSet.getTrees()) {
            List<TreeNode> linearTree = currentTree.getLinearRepresentation();
            
            for (TreeNode currentNode: linearTree) {
                currentPosition = fetchToken(text, currentPosition, currentNode);
            }
        }
        
    }
    
    private int fetchToken(AnotatedText text, int position, TreeNode currentNode) throws TextMatchingException {
        String originalText = text.getText();
        int currPos = skipWhiteSpace(originalText, position);
        List<EntityAnotation> entities = text.getEntitiesAtPosition(currPos);
        
        for (EntityAnotation entity: entities) {
            int entityId = entity.getId();
            System.err.println(currentNode.toString() + " detected as part of an entity " + entityId);
            currentNode.addToEntity(entityId);
            entity.assignNode(currentNode);
        }
        
        String pattern = currentNode.getContentInEntity(-1);
        for (int i = 0; i < pattern.length(); i++) {
            if (originalText.charAt(currPos + i) != pattern.charAt(i)) {
                // Still have to check whether whitespaces inside token weren't eliminated
                int skippedPos = skipWhiteSpace(originalText, currPos + i);
                if (originalText.charAt(skippedPos) != pattern.charAt(i)) {
                    throw new TextMatchingException("Characters " + originalText.charAt(currPos + i) + " and " + pattern.charAt(i) + " don't match!");
                }
                else {
                    currPos = skippedPos - i;
                }
            }
        }
        int finalPos = skipWhiteSpace(originalText, currPos + pattern.length());
        return finalPos;
    }
    
    private int skipWhiteSpace(String text, int position) {
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }
        return position;
    }
}
