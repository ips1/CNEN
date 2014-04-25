/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.anotations;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author petrkubat
 */
public class EntityAnotation {
    private final int begin;
    private final int end;
    private final int id;
    private final String attributes;
    private String normalizedName;
    private List<TreeNode> entityNodes;

    public EntityAnotation(int begin, int end, int id, String attributes) {
        this.begin = begin;
        this.end = end;
        this.id = id;
        this.attributes = attributes;
        this.entityNodes = new ArrayList<TreeNode>();
    }
    
    public int getBegin() {
        return begin;
    }
    
    public int getEnd() {
        return end;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean containsPosition(int pos) {
        return (pos >= begin || pos < end);
    } 
    
    public void assignNode(TreeNode node) {
        // Only assigning nodes with corresponding ID
        if (node.getEntityId() != id) return;
        
        int i = 0;
        
        while (i < entityNodes.size() && entityNodes.get(i).getOrder() < node.getOrder()) {
            i++;
        }
        
        if (i < entityNodes.size() && entityNodes.get(i).getOrder() == node.getOrder()) {
            // Error, two nodes can't have the same order, probably trying to assign one node twice
            return;
        }
        
        entityNodes.add(i, node);
    }
    
    public void fetchNormalizedName() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < entityNodes.size(); i++) {
            if (i == entityNodes.size() - 1) {
                // Last one printed without space
                output.append(entityNodes.get(i).getContent());
            }
            else {
                output.append(entityNodes.get(i).toString());
            }
        }
        normalizedName = output.toString();
    }
    
    public void setNormalizedName(String newName) {
        normalizedName = newName;
    }
    
    public String getNormalizedName() {
        return normalizedName;
    }
    
    public String getAttributes() {
        return attributes;
    }
}
