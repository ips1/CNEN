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

    public TreeNode(String id, int order, boolean spaceAfter, String content, String lemma, Tag tag) {
        this.id = id;
        this.order = order;
        this.spaceAfter = spaceAfter;
        this.content = content;
        this.lemma = lemma;
        this.tag = tag;
        this.children = new ArrayList<TreeNode>();
        this.childCount = 0;
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
