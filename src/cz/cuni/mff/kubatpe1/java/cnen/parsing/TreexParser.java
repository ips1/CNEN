
package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMException;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMLoader;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeNode;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.AnalyticalFunction;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.InvalidTagException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for parsing the treex files with m-layer and a-layer text analysis
 * into SentenceTrees.
 * @author Petr Kubat
 */
public class TreexParser implements SentenceTreeParser {

    // treex element names
    private static final String TREE_ROOT = "a_tree";
    private static final String ID_ATTR = "id";
    private static final String CHILDREN_ELEM = "children";
    private static final String FORM_ELEM = "form";
    private static final String LEMMA_ELEM = "lemma";
    private static final String TAG_ELEM = "tag";
    private static final String ORD_ELEM = "ord";
    private static final String NO_SPACE_ELEM = "no_space_after";
    private static final String AFUN_ELEM = "afun";
    private static final String IS_MEMEBER_ELEM = "is_member";
    
    // Default content of a tag
    private static final String DEF_TAG = "---------------";
    
    @Override
    public SentenceCollection parseDocument(String path) throws TreeParsingException {
        try {
            Document doc = DOMLoader.loadDOM(path);
            return parseDOM(doc);
        } catch (DOMException ex) {
            throw new TreeParsingException(ex);
        }
    }
    
    /**
     * Parses SentenceCollection from a treex DOM document.
     * Must have specific Treex format!
     * @param doc Document to parse from.
     * @return Parsed SentenceCollection.
     * @throws TreeParsingException Parsing failed.
     */
    private SentenceCollection parseDOM(Document doc) throws TreeParsingException {
        // Searching for a root element
        NodeList rootList = doc.getElementsByTagName(TREE_ROOT);
        
        SentenceCollection collection = new SentenceCollection();
        
        for (int i = 0; i < rootList.getLength(); i++) {
            Element root = (Element)rootList.item(i);

            // Parsing node from root element
            TreeNode rootNode = fetchNode(root);

            SentenceTree tree = new SentenceTree(rootNode);
            
            collection.addTree(tree);
        }
        
        return collection;
    }
    
    /**
     * Parses one node (and recursively its sub-nodes) from an element representing a node.
     * @param elem DOM element representing the node root
     * @return Parsed TreeNode (containing its sub-nodes)
     * @throws TreeParsingException Node can't be parsed
     */
    private TreeNode fetchNode(Element elem) throws TreeParsingException {
        String id = elem.getAttribute(ID_ATTR);
        
        NodeList subElems = elem.getChildNodes();
        
        int order = 0;
        boolean spaceAfter = false;
        
        String content = "";
        String lemma = "";
        String tagString = DEF_TAG;
        String afunString = "";
        
        boolean isMember = false;
        
        Element childrenElement = null;
        
        // Iterating through all the children
        for (int i = 0; i < subElems.getLength(); i++) {
            Node current = subElems.item(i);
            
            // Node not an element
            if (current.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            
            String elemName = current.getNodeName();
            // Processing the element
            if(elemName.equals(CHILDREN_ELEM)) {
                if (childrenElement != null) {
                    throw new TreeParsingException("Element contains two children subelements");
                }   
                childrenElement = (Element)current;
            }
            else if (elemName.equals(ORD_ELEM)) {
                try {
                    order = Integer.parseInt(getNodeContent(current));
                }
                catch (NumberFormatException ex) {
                    throw new TreeParsingException(ex);
                }   
            }
            else if (elemName.equals(FORM_ELEM)) {
                content = getNodeContent(current);
            }
            else if (elemName.equals(LEMMA_ELEM)) {
                lemma = getNodeContent(current);
            }
            else if (elemName.equals(TAG_ELEM)) {
                tagString = getNodeContent(current);
            }
            else if (elemName.equals(AFUN_ELEM)) {
                afunString = getNodeContent(current);
            }
            else if (elemName.equals(IS_MEMEBER_ELEM)) {
                isMember = true;
            }
            else if (elemName.equals(NO_SPACE_ELEM)) {
                try {
                    spaceAfter = Integer.parseInt(getNodeContent(current)) <= 0;
                }
                catch (NumberFormatException ex) {
                    throw new TreeParsingException(ex);
                }   
            }
                   
        }
        
        Tag t;
        try {
            t = new Tag(tagString);
        } 
        catch (InvalidTagException ex) {
            throw new TreeParsingException(ex);
        }
        
        AnalyticalFunction afun = new AnalyticalFunction(afunString, isMember);
        
        TreeNode currentNode = new TreeNode(id, order, spaceAfter, content, lemma, t, afun);
        
        // If contains children element, we have to parse children
        if (childrenElement != null) {
            // Single child
            if (!childrenElement.getAttribute(ID_ATTR).equals("")) {
                TreeNode newNode = fetchNode(childrenElement);
                currentNode.addChild(newNode);
            }
            // Multiple children
            else {
                NodeList children = childrenElement.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node n = children.item(i);            
                    // Node not an element
                    if (n.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    TreeNode newNode = fetchNode((Element)n);
                    
                    currentNode.addChild(newNode);
                }
            }
        }
        
        return currentNode;
        
        
    }
   
    private String getNodeContent(Node n) throws TreeParsingException {
        // Fetches textual content of a single node in DOM tree
        NodeList childNodes = n.getChildNodes();
        if (childNodes.getLength() != 1) {
            throw new TreeParsingException("Invalid element content");
        }
        return (childNodes.item(0).getNodeValue());
    }

    /**
     * Parses one document and returns it as a one-element list.
     * @param path Document to parse from.
     * @return List containing single SentenceCollection.
     * @throws TreeParsingException Parsing failed.
     */
    @Override
    public List<SentenceCollection> parseDocumentSet(String path) throws TreeParsingException {
        List<SentenceCollection> result = new ArrayList<SentenceCollection>();
        result.add(parseDocument(path));
        return result;
    }
    
}