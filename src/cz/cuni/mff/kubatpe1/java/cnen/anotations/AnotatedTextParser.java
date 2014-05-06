/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.anotations;

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
 *
 * @author petrkubat
 */
public class AnotatedTextParser {
    private final String input;
    private final String entityTag;
    private StringBuilder text;
    private List<EntityAnotation> anotations;
    private int currentId;
    
    public AnotatedTextParser(String input, String entityTag) {
        this.input = input;
        this.entityTag = entityTag;
    }
    
    public AnotatedText parseText() throws AnotationParsingException {
        this.text = new StringBuilder();
        this.anotations = new ArrayList<EntityAnotation>();
        this.currentId = 0;
        
        Document doc = null;
        try {
            doc = DOMLoader.loadDOM(input);
        } catch (DOMException ex) {
            throw new AnotationParsingException("Cannot parse input file! Invalid XML format.", ex);
        }
        
        Element root = doc.getDocumentElement();
     
        parseNode(root);
        
        return new AnotatedText(doc, text.toString(), anotations);
    }
    
    private void parseNode(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            text.append(node.getTextContent());
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element)node;
            if (elem.getTagName().equals(entityTag)) {
                // Inside an entity anotation
                int start = text.length();
                int id = currentId++;
                
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    parseNode(children.item(i));
                }
                
                int end = text.length();
                
                anotations.add(new EntityAnotation(start, end, id, elem));
            }
            else {
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
