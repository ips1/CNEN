
package cz.cuni.mff.kubatpe1.java.cnen.dom;

import java.io.File;
import java.io.IOException;
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
 * Class with static methods for loading and storing DOM from XML.
 * Wrapper to the standard org.w3c.dom and org.xml.sax classes.
 * @author petrkubat
 */
public class DOMLoader {
    
    /**
     * Loads DOM document from specified XML file.
     * @param path Path to the input file.
     * @return DOM document loaded from the file.
     * @throws DOMException Loading of the document failed.
     */
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
    
    /**
     * Saves DOM document to specified XML file.
     * @param path Path to the output file.
     * @param document Document to be saved.
     * @throws DOMException Saving the document failed.
     */
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
