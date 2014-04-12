/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Petr
 */
public class SentenceTree {
    private TreeNode root;
    private int nodeCount; 

    public SentenceTree(TreeNode root) {
        this.root = root;
        this.nodeCount = root.getChildCount() + 1;
    }
    
    public TreeNode getRoot() {
        return root;
    }
    
    public List<TreeNode> getLinearRepresentation() {
        List<TreeNode> list = new ArrayList<TreeNode>();
        
        /* Setting the size of the list */
        for (int i = 0; i < nodeCount; i++) {
            list.add(null);
        }
        
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        
        while (!stack.empty()) {
            TreeNode current = stack.pop();
            for (TreeNode child: current.getChildren()) {
                stack.push(child);
            }
            
            // TODO: implement security
            list.set(current.getOrder(), current);
        }
        
        return list;
    }
    
    @Override
    public String toString() {
        List<TreeNode> list = getLinearRepresentation();
        
        StringBuilder sb = new StringBuilder();
        
        for (TreeNode n: list) {
            sb.append(n.toString());
        }
        
        return sb.toString();
    }
    
}
