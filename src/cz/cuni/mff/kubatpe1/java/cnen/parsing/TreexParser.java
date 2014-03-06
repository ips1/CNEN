/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.Tag;
import cz.cuni.mff.kubatpe1.java.cnen.TreeNode;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Petr
 */
public class TreexParser implements SentenceTreeParser {

    private static final String TREE_ROOT = "a_tree";
    private static final String ID_ATTR = "id";
    private static final String CHILDREN_ELEM = "children";
    private static final String FORM_ELEM = "form";
    private static final String LEMMA_ELEM = "lemma";
    private static final String TAG_ELEM = "tag";
    private static final String ORD_ELEM = "ord";
    
    
    @Override
    public SentenceTree parseTree(String path) throws TreeParsingException {
        Document doc = loadDOM(path);
        SentenceTree t = parseDOM(doc);
        
        return t;
    }
    
    private Document loadDOM(String path) throws TreeParsingException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);

            DocumentBuilder builder = dbf.newDocumentBuilder();

            doc = builder.parse(path);
        }
        catch (SAXException ex) {
            throw new TreeParsingException(ex);
        } catch (IOException ex) {
            throw new TreeParsingException(ex);
        } catch (ParserConfigurationException ex) {
            throw new TreeParsingException(ex);
        }
        
        return doc;
    }
    
    /**
     * Parses sentence tree from a DOM document.
     * Must have specific Treex format!
     * @param doc Document to parse from
     * @return Parsed sentence tree
     * @throws TreeParsingException Document can't be parsed
     */
    private SentenceTree parseDOM(Document doc) throws TreeParsingException {
        // Searching for a root element
        NodeList rootList = doc.getElementsByTagName(TREE_ROOT);
        // Document must have exactly one root
        if (rootList.getLength() != 1) throw new TreeParsingException();
        
        Element root = (Element)rootList.item(0);
        
        // Parsing node from root element
        TreeNode rootNode = fetchNode(root);
        
        SentenceTree tree = new SentenceTree(rootNode);
        
        return tree;
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
        String content = "";
        String lemma = "";
        String tagString = "";
        
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
            switch (elemName) {
                case CHILDREN_ELEM:
                    if (childrenElement != null) {
                        throw new TreeParsingException("Element contains two children subelements");
                    }   
                    childrenElement = (Element)current;
                    break;
                case ORD_ELEM:
                    try {
                        order = Integer.parseInt(getNodeContent(current));
                    }
                    catch (NumberFormatException ex) {
                        throw new TreeParsingException(ex);
                    }   
                    break;
                case FORM_ELEM:
                    content = getNodeContent(current);
                    break;
                case LEMMA_ELEM:
                    lemma = getNodeContent(current);
                    break;
            }
                   
        }
        
        TreeNode currentNode = new TreeNode(id, order, content, lemma, new Tag(tagString));
        
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
        NodeList childNodes = n.getChildNodes();
        if (childNodes.getLength() != 1) {
            throw new TreeParsingException("Invalid element content");
        }
        return (childNodes.item(0).getNodeValue());
    }
    
}
