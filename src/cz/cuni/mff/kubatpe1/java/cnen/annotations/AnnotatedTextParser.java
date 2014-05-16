
package cz.cuni.mff.kubatpe1.java.cnen.annotations;

import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMException;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMLoader;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for generating AnnotatedText from XML documents.
 * @author Petr Kubat
 */
public class AnnotatedTextParser {
    // Input file path
    private final String input;
    // Element for named entity anotation
    private final String entityTag;
    // Should spaces be added around anotations in original text
    private boolean addSpaces;
    // Builder for the original text reconstruction
    private StringBuilder text;
    // List of all entities
    private List<EntityAnnotation> anotations;
   
    private int currentId;
    
    /**
     * Default constructor for AnnotatedTextParser class.
     * @param input Input file name.
     * @param entityTag Element for named entity annotation (e.g. "ne").
     */
    public AnnotatedTextParser(String input, String entityTag) {
        this.input = input;
        this.entityTag = entityTag;
        this.addSpaces = false;
    }
    
    /**
     * Parses the input document and creates AnnotatedText.
     * @param addSpaces Boolean value determining whether spaces should be added
     * around the entities (which would make them a separate token).
     * @return AnnotatedText fetched from the XML document.
     * @throws AnnotationParsingException Parsing of the XML failed.
     */
    public AnnotatedText parseText(boolean addSpaces) throws AnnotationParsingException {
        this.text = new StringBuilder();
        this.anotations = new ArrayList<EntityAnnotation>();
        this.currentId = 0;
        this.addSpaces = addSpaces;
        
        // Load DOM from external file
        Document doc = null;
        try {
            doc = DOMLoader.loadDOM(input);
        } catch (DOMException ex) {
            throw new AnnotationParsingException("Cannot parse input file! Invalid XML format.", ex);
        }
        
        // Recursively parse all nodes
        Element root = doc.getDocumentElement();
     
        parseNode(root);
        
        return new AnnotatedText(doc, text.toString(), anotations);
    }
    
    private void parseNode(Node node) {
        // Text nodes are appended
        if (node.getNodeType() == Node.TEXT_NODE) {
            text.append(node.getTextContent());
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element)node;
            // Named entity elements add new anotation record
            if (elem.getTagName().equals(entityTag)) {
                // Inside an entity anotation
                // We have reached start index of the anotation
                if (addSpaces) text.append(' ');
                int start = text.length();
                int id = currentId++;
                
                // Recursively parsing children
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    parseNode(children.item(i));
                }
                // Returned from the subtree back, we have reached end index of 
                // the anotation
                int end = text.length();
                
                anotations.add(new EntityAnnotation(start, end, id, elem));
                if (addSpaces) text.append(' ');
            }
            else {
                // Recursively parsing children
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    parseNode(children.item(i));
                }
            }
        }
        else {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                parseNode(children.item(i));
            }
        }
    }
}
