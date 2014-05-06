/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.dom;

import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author petrkubat
 */
public class DOMLoader {
    public static Document loadDOM(String path) throws DOMException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);

            DocumentBuilder builder = dbf.newDocumentBuilder();

            doc = builder.parse(path);
        }
        catch (SAXException ex) {
            throw new DOMException(ex);
        } catch (IOException ex) {
            throw new DOMException(ex);
        } catch (ParserConfigurationException ex) {
            throw new DOMException(ex);
        }
        
        return doc;
    }
    
    public static void saveDOM(String path, Document document) throws DOMException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            Result output = new StreamResult(new File(path));
            Source input = new DOMSource(document);
            
            transformer.transform(input, output);
        } catch (TransformerConfigurationException ex) {
            throw new DOMException(ex);
        } catch (TransformerException ex) {
            throw new DOMException(ex);
        }
    }
}
