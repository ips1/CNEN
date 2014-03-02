/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;


import java.util.*;

/**
 *
 * @author Petr Kubat
 */
public class TreeNode {
    private String id;
    private int order;
    private String content;
    private String lemma;
    private Tag tag;
    private List<TreeNode> children;

    public TreeNode(String id, int order, String content, String lemma, Tag tag) {
        this.id = id;
        this.order = order;
        this.content = content;
        this.lemma = lemma;
        this.tag = tag;
        this.children = new ArrayList<TreeNode>();
    }
    
    public void addChild(TreeNode child) {
        this.children.add(child);
    }
    
}
