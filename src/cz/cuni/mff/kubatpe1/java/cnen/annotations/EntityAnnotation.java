
package cz.cuni.mff.kubatpe1.java.cnen.annotations;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Class representing one named entity annotated in the text.
 * 
 * @author Petr Kubat
 */
public class EntityAnnotation {
    // Offsets of the anotation in original text
    private final int begin;
    private final int end;
    // Unique id
    private final int id;
    // Reference to the DOM document
    private final Element anotationElement;
    
    private String normalizedName;
    
    // Nodes of whichc the entity consists in the SentenceTree
    // filled by TreeTextMatcher class
    private final List<TreeNode> entityNodes;

    /**
     * Default constructor for the EntityAnotation class.
     * @param begin Start index of the annotation.
     * @param end End index of the annotation.
     * @param id Unique ID for the annotation.
     * @param anotationElement DOM element representing the annotation.
     */
    public EntityAnnotation(int begin, int end, int id, Element anotationElement) {
        this.begin = begin;
        this.end = end;
        this.id = id;
        this.anotationElement = anotationElement;
        this.entityNodes = new ArrayList<TreeNode>();
    }
    
    /**
     * Getter for the begin index.
     * @return Index where the annotation begins in original text.
     */
    public int getBegin() {
        return begin;
    }
    
    /**
     * Getter for the end index.
     * @return Index where the annotation ends in original text.
     */
    public int getEnd() {
        return end;
    }
    
    /**
     * Getter for the annotation ID.
     * @return ID of the annotation.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Checks whether the annotation contains certain position in the text.
     * @param pos Index to be checked.
     * @return True if pos is contained within the entity, false otherwise.
     */
    public boolean containsPosition(int pos) {
        return (pos >= begin || pos < end);
    } 
    
    /**
     * Assigns a SentenceTree node to this entity.
     * Node is treated as a part of the entity afterwards.
     * @param node TreeNode to be assigned.
     */
    public void assignNode(TreeNode node) {
        // Only assigning nodes with corresponding ID
        if (!node.isInEntity(id)) return;
        
        int i = 0;
        
        // Nodes are stored in their order in sentence
        while (i < entityNodes.size() && entityNodes.get(i).getOrder() < node.getOrder()) {
            i++;
        }
        
        if (i < entityNodes.size() && entityNodes.get(i).getOrder() == node.getOrder()) {
            // Error, two nodes can't have the same order, probably trying to assign one node twice
            return;
        }
        
        entityNodes.add(i, node);
    }
    
    /**
     * Fetches the entity normalized name from all assigned SentenceTree nodes.
     * Should be called after all TreeActions performing normalization have
     * finished running on the SentenceTree.
     * All the TreeNodes of which the entity consists have to be assigned
     * before.
     */
    public void fetchNormalizedName() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < entityNodes.size(); i++) {
            if (i == entityNodes.size() - 1) {
                // Last one printed without space
                output.append(entityNodes.get(i).getContentInEntity(id));
            }
            else {
                output.append(entityNodes.get(i).stringFromEntity(id));
            }
        }
        normalizedName = output.toString();
        
        System.err.println("Entity " + id + ": " + normalizedName);
    }
    
    /**
     * Sets the entity normalized name manually.
     * This is an alternative to the fetchNormalizedName() method.
     * @param newName Normalized name to be set.
     */
    public void setNormalizedName(String newName) {
        normalizedName = newName;
    }
    
    /**
     * Gets current normalized name of the entity.
     * @return Current normalized name.
     */
    public String getNormalizedName() {
        return normalizedName;
    }
    
    /**
     * Gets the DOM element for this annotation.
     * @return DOM element for this annotation.
     */
    public Element getAnotationElement() {
        return anotationElement;
    }
}
