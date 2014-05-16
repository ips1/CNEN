
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;


import java.util.*;

/**
 * Class representing one node in the sentence tree of a Czech sentence 
 * according to PDT.
 * Each node carries several word forms:
 *  - one original word form
 *  - one default word form (for default tree actions)
 *  - list of word forms identified by entity ids
 * Children in the tree are held in unordered lists. Their order is determined
 * by the order attribute of nodes.
 * @author Petr Kubat
 */
public class TreeNode {
    
    // Entity ID for a default form
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

    /**
     * Default constructor for a TreeNode class.
     * @param id String identifier of the node.
     * @param order Integer representing the position in original sentence.
     * @param spaceAfter Boolean value determining whether there should follow
     * a space after the token represented by the node.
     * @param content Original textual content of the node (i.e. the sentence 
     * token).
     * @param lemma Original lemma for the node.
     * @param tag Original tag for the node.
     * @param afun Analytical function of the node in the tree.
     */
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
    
    /**
     * Assigns a child to the node.
     * @param child Node to assign as child.
     */
    public void addChild(TreeNode child) {
        this.children.add(child);
        childCount += child.getChildrenCount() + 1;
    }
    
    /**
     * Gets the children count of this node.
     * @return Children count of this node.
     */
    public int getChildrenCount() {
        return childCount;
    }
    
    /**
     * Gets iterable list of all children of the node.
     * @return List of all children of the node.
     */
    public List<TreeNode> getChildren() {
        return children;
    }
    
    /**
     * Gets the order of the node.
     * @return Order of the node.
     */
    public int getOrder() {
        return order;
    }
    
    /**
     * Gets tag for the default form.
     * @return Tag for the default form.
     */
    public Tag getDefaultFormTag() {
        return defaultForm.tag;
    }
    
    /**
     * Gets lemma for the default form.
     * @return Lemma for the default form.
     */
    public String getDefaultFormLemma() {
        return defaultForm.lemma;
    }
    
    /**
     * Gets tag for the original form.
     * @return Tag for the original form.
     */
    public Tag getOriginalTag() {
        return originalForm.tag;
    }
    
    /**
     * Gets tag for form in specified entity.
     * If entity with this ID doesn't have own form, returns default form tag.
     * @param entityId ID of entity.
     * @return Tag for form in entity with specified ID.
     */
    public Tag getTagInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.tag;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.tag;
        }
        return entityForms.get(entityId).tag;
    }
    
    /**
     * Gets lemma for form in specified entity.
     * If entity with this ID doesn't have own form, returns default form lemma.
     * @param entityId ID of entity.
     * @return Lemma for form in entity with specified ID.
     */
    public String getLemmaInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.lemma;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.lemma;
        }
        return entityForms.get(entityId).lemma;
    }
    
    /**
     * Gets content for form in specified entity.
     * If entity with this ID doesn't have own form, returns default form 
     * content.
     * @param entityId ID of entity.
     * @return Content for form in entity with specified ID.
     */
    public String getContentInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.content;
        }
        if (!this.isInEntity(entityId)) {
            return defaultForm.lemma;
        }
        return entityForms.get(entityId).content;
    }
    
    /**
     * Sets content for form in specified entity.
     * @param entityId ID of entity.
     * @param content New content for form in entity.
     */
    public void setContentInEntity(int entityId, String content) {
        if (entityId == defaultFormId) {
            defaultForm.content = content;
            return;
        }
        entityForms.get(entityId).content = content;    
    }
    
    /**
     * Finds out whether the node is in entity with specified ID.
     * @param entityId ID of entity.
     * @return True if the node is assigned to the entity.
     */
    public boolean isInEntity(int entityId) {
        if (entityId == defaultFormId) return true;
        return entityForms.containsKey(entityId);
    }
    
    /**
     * Assigns the node to a specified entity.
     * @param entityId ID of entity.
     */
    public void addToEntity(int entityId) {
        if (entityId == defaultFormId) return;
        if (entityForms.containsKey(entityId)) return;
        entityForms.put(entityId, defaultForm.getCopy());
    }
    
    /**
     * Gets IDs of all entities to which node belongs.
     * @return List of entity IDs.
     */
    public List<Integer> getEntityIds() {
        return new ArrayList(entityForms.keySet());
    }
    
    /**
     * Gets the descendant node count.
     * Finds out the count of all nodes in specified node's subtree.
     * @return Count of descendants.
     */
    public int countNodes() {
        int result = 0;
        for (TreeNode child: children) {
            result += (child.countNodes() + 1);
        }
        return result;
    }
    
    /**
     * Finds out whether the node has been normalized in specified entity.
     * @param entityId ID of entity.
     * @return True if the node has been normalized.
     */
    public boolean isNormalizedInEntity(int entityId) {
        if (entityId == defaultFormId) {
            return defaultForm.normalized;
        }
        return entityForms.get(entityId).normalized;
    }
    
    /**
     * Sets the node as normalized in specific entity.
     * @param entityId ID of entity.
     * @param normalized Value to which normalized should be set.
     */
    public void setNormalizedInEntity(int entityId, boolean normalized) {
        if (entityId == defaultFormId) {
            defaultForm.normalized = normalized;
            return;
        }
        entityForms.get(entityId).normalized = normalized;
    }
    
    /**
     * Gets the string content from word form in specified entity.
     * @param entityId ID of entity.
     * @return String with current word form in the entity.
     */
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
    
    /**
     * Gets analytical function of the node.
     * @return AnalyticalFunction for the node.
     */
    public AnalyticalFunction getAfun() {
        return afun;
    }
    
    /**
     * Gets the string content from default word form.
     * @return String with current default word form.
     */
    @Override
    public String toString() {
        return stringFromEntity(defaultFormId);
    }
    
}
