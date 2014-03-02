/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.SentenceTree;
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

    const String TREE_ROOT = "a_tree";
    const String ID_ATTR = "id";
    
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
    
    private SentenceTree parseDOM(Document doc) throws TreeParsingException {
        NodeList rootList = doc.getElementsByTagName(TREE_ROOT);
        // Document must have exactly one root
        if (rootList.getLength() != 1) throw new TreeParsingException();
        
        
    }
    
    private TreeNode fetchNode(Element elem) throws TreeParsingException {
        String id = elem.getAttribute(ID_ATTR);
        
        NodeList children = elem.getChildNodes();
        
        int order;
        String content;
        String lemma;
        
        TreeNode n = new TreeNode(id, order, TREE_ROOT, TREE_ROOT)
    }
    
}
