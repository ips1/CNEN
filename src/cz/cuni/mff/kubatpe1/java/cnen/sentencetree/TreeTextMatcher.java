/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.EntityAnotation;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.TextMatchingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author petrkubat
 */
public class TreeTextMatcher {

    private List<SentenceTree> treeSet;
    
    public TreeTextMatcher(List<SentenceTree> treeSet) {
        this.treeSet = treeSet;
    }
    
    public TreeTextMatcher() {
        treeSet = new ArrayList<SentenceTree>();
    }
    
    public void clearTreeSet() {
        treeSet.clear();
    }
    
    public void addTree (SentenceTree tree) {
        treeSet.add(tree);
    }
    
    public void matchWithText(AnotatedText text) throws TextMatchingException {
        // Assuming there is unlimited white space between tokens
        
        int currentPosition = 0;
        for (SentenceTree currentTree: treeSet) {
            List<TreeNode> linearTree = currentTree.getLinearRepresentation();
            
            for (TreeNode currentNode: linearTree) {
                currentPosition = fetchToken(text, currentPosition, currentNode);
            }
        }
        
    }
    
    private int fetchToken(AnotatedText text, int position, TreeNode currentNode) throws TextMatchingException {
        String originalText = text.getText();
        int currPos = skipWhiteSpace(originalText, position);
        EntityAnotation entity = text.getEntityAtPosition(currPos);
        int entityId;
        
        if (entity != null) {
            entityId = entity.getId();
            System.out.println(currentNode.toString() + " detected as part of an entity " + entityId);
            currentNode.setEntityId(entityId);
            entity.assignNode(currentNode);
        }
        else {
            entityId = -1;
        }
        
        String pattern = currentNode.getContent();
        for (int i = 0; i < pattern.length(); i++) {
            if (originalText.charAt(currPos + i) != pattern.charAt(i)) {
                throw new TextMatchingException();
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
