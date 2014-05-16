
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class representing an a-layer dependency tree of a Czech sentence according 
 * to PDT.
 * @author Petr Kubat
 */
public class SentenceTree {
    
    private final TreeNode root;
    private final int nodeCount; 

    /**
     * Default constructor for the SentenceTree class.
     * Takes a root node for a fully constructed tree.
     * @param root 
     */
    public SentenceTree(TreeNode root) {
        this.root = root;
        this.nodeCount = root.getChildrenCount() + 1;
    }
    
    /**
     * Gets the root node.
     * @return Root node of the tree.
     */
    public TreeNode getRoot() {
        return root;
    }
    
    /**
     * Linearizes the tree according to the original sentence ordering.
     * @return List of nodes sorted according to the original sentence ordering.
     */
    public List<TreeNode> getLinearRepresentation() {
        List<TreeNode> list = new ArrayList<TreeNode>();

        // Setting the list size
        for (int i = 0; i < nodeCount; i++) {
            list.add(null);
        }
        
        // Tree DFS
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        
        while (!stack.empty()) {
            TreeNode current = stack.pop();
            for (TreeNode child: current.getChildren()) {
                stack.push(child);
            }
            
            list.set(current.getOrder(), current);
        }
        
        return list;
    }
    
    /**
     * Reconstructs the sentence from the tree.
     * @return String containing the sentence.
     */
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
