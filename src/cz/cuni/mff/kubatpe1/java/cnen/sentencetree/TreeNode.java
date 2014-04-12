/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;


import java.util.*;

/**
 *
 * @author Petr Kubat
 */
public class TreeNode {
    
    private int childCount;
    
    private String id;
    private int order;
    private boolean spaceAfter;
    private String content;
    private String lemma;
    private Tag tag;
    private List<TreeNode> children;
    
    private int entityId;
    private boolean normalized;

    public TreeNode(String id, int order, boolean spaceAfter, String content, String lemma, Tag tag) {
        this.id = id;
        this.order = order;
        this.spaceAfter = spaceAfter;
        this.content = content;
        this.lemma = lemma;
        this.tag = tag;
        this.children = new ArrayList<TreeNode>();
        this.childCount = 0;
        this.entityId = -1;
        this.normalized = false;
    }
    
    public void addChild(TreeNode child) {
        this.children.add(child);
        childCount += child.getChildCount() + 1;
    }
    
    public int getChildCount() {
        return childCount;
    }
    
    public List<TreeNode> getChildren() {
        return children;
    }
    
    public int getOrder() {
        return order;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public String getLemma() {
        return lemma;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getEntityId() {
        return entityId;
    }
    
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
    
    public int countNodes() {
        int result = 0;
        for (TreeNode child: children) {
            result += (child.countNodes() + 1);
        }
        return result;
    }
    
    public boolean isNormalized() {
        return normalized;
    }
    
    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }
    
    @Override
    public String toString() {
        if (spaceAfter) {
            return content + ' ';
        }
        else {
            return content;
        }
    }
    
}
