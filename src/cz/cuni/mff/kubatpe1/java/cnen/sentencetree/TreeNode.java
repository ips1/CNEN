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
    private static final int defaultFormId = -1;
    
    private int childCount;
    
    // Identifier of the node
    private final String id;
    private final int order;
    private final boolean spaceAfter;
    private final AnalyticalFunction afun;
    // Form for the original word
    private final WordForm originalForm;
    // Default form for the word, might be changed
    private final WordForm defaultForm;
    // Forms for all entities which the word is part of
    private final Map<Integer, WordForm> entityForms;
    private final List<TreeNode> children;

    public TreeNode(String id, int order, boolean spaceAfter, String content, String lemma, Tag tag, AnalyticalFunction afun) {
        this.id = id;
        this.order = order;
        this.spaceAfter = spaceAfter;
        this.afun = afun;
        this.originalForm = new WordForm(content, lemma, tag);
        this.defaultForm = originalForm.getCopy();
        this.children = new ArrayList<TreeNode>();
        this.childCount = 0;
        
        this.entityForms = new HashMap<Integer, WordForm>();
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
    
    public Tag getDefaultFormTag() {
        return defaultForm.tag;
    }
    
    public String getDefaultFormLemma() {
        return defaultForm.lemma;
    }
    
    public Tag getTagInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.tag;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.tag;
        }
        return entityForms.get(entityId).tag;
    }
    
    public String getLemmaInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.lemma;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.lemma;
        }
        return entityForms.get(entityId).lemma;
    }
    
    public String getContentInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.content;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.lemma;
        }
        return entityForms.get(entityId).content;
    }
    
    public void setContentInEntity(int entityId, String content) {
        if (entityId == defaultFormId) {
            defaultForm.content = content;
            return;
        }
        entityForms.get(entityId).content = content;    
    }
    
    public boolean isInEntity(int entityId) {
        if (entityId == defaultFormId) return true;
        return entityForms.containsKey(entityId);
    }
    
    public void addToEntity(int entityId) {
        if (entityId == defaultFormId) return;
        if (entityForms.containsKey(entityId)) return;
        entityForms.put(entityId, defaultForm.getCopy());
    }
    
    public List<Integer> getEntityIds() {
        return new ArrayList(entityForms.keySet());
    }
    
    public int countNodes() {
        int result = 0;
        for (TreeNode child: children) {
            result += (child.countNodes() + 1);
        }
        return result;
    }
    
    public boolean isNormalizedInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.normalized;
        }
        return entityForms.get(entityId).normalized;
    }
    
    public void setNormalizedInEntity(int entityId, boolean normalized) {
        if (entityId == defaultFormId) {
            defaultForm.normalized = normalized;
            return;
        }
        entityForms.get(entityId).normalized = normalized;
    }
    
    public String stringFromEntity(int entityId) {
        String str = null;
        if (entityId == defaultFormId) {
            str = defaultForm.content;
        }
        else {
            str = entityForms.get(entityId).content;
        }
        
        return (spaceAfter ? str + ' ' : str);
    }
    
    @Override
    public String toString() {
        return stringFromEntity(defaultFormId);
    }
    
}
