
package cz.cuni.mff.kubatpe1.java.cnen.annotations;

import cz.cuni.mff.kubatpe1.java.cnen.annotations.AnnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.annotations.EntityAnnotation;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.SentenceCollection;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.TextMatchingException;
import java.util.List;

/**
 * Class for matching AnnotatedText with SenteceTree collection.
 * Matches each entity with a set of TreeNodes of which it consists.
 * @author petrkubat
 */
public class TreeTextMatcher {

    // Collection of SentenceTrees to be matched
    private SentenceCollection treeCollection;
    
    /**
     * Default constructor for the TreeTextMatcher class.
     * @param treeCollection Collection of SentenceTrees to be used for matching.
     */
    public TreeTextMatcher(SentenceCollection treeCollection) {
        this.treeCollection = treeCollection;
    }
    
    /**
     * Matches SentenceTree collection stored in the matcher with specified
     * text.
     * For each EntityAnnotation in AnnotatedText, a set of TreeNodes of which
 it consists is assigned.
     * @param text AnnotatedText to be matched.
     * @throws TextMatchingException Text doesn't correspond to the trees.
     */
    public void matchWithText(AnnotatedText text) throws TextMatchingException {
        // Assuming there is unlimited white space between tokens
        
        int currentPosition = 0;
        for (SentenceTree currentTree: treeCollection.getTrees()) {
            List<TreeNode> linearTree = currentTree.getLinearRepresentation();
            
            // Skipping the first node as it is the unused root
            for (int i = 1; i < linearTree.size(); i++) {
                currentPosition = fetchToken(text, currentPosition, linearTree.get(i));
            }
        }
        
    }
    
    private int fetchToken(AnnotatedText text, int position, TreeNode currentNode) throws TextMatchingException {
        // Matches one TreeNode with part of the text, returns new position
        String originalText = text.getText();
        int currPos = skipWhiteSpace(originalText, position);
        List<EntityAnnotation> entities = text.getEntitiesAtPosition(currPos);
        
        for (EntityAnnotation entity: entities) {
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
        // Skips all white space in specified String at specified position
        // Returns new position
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }
        return position;
    }
}
