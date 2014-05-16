
package cz.cuni.mff.kubatpe1.java.cnen.annotations;

import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMException;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMLoader;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing a text with named entity annotations.
 * It is always fetched from an XML document.
 * It consists of the original XML document, plaintext and a set of annotaitons.
 * @author petrkubat
 */
public class AnnotatedText {
    // DOM of original XML document
    private final Document DOMDocument;
    // Plaintext content
    private final String originalText;
    // Entity anotations in the text
    private final List<EntityAnnotation> anotations;
    
    /**
     * Default constructor of AnonatedText class.
     * @param DOMDocument DOM source of the text.
     * @param originalText Plaintext representation.
     * @param anotations Entity anotations according to the originalText.
     */
    public AnnotatedText(Document DOMDocument, String originalText, List<EntityAnnotation> anotations) {
        this.DOMDocument = DOMDocument;
        this.originalText = originalText;
        this.anotations = anotations;
    }
    
    /**
     * Returns the plaintext representation.
     * @return Plaintext representation.
     */
    public String getText() {
        return originalText;
    }
    
    /**
     * Finds out which entities are annotated at specified position in the text.
     * If no entity is marked, returns empty list.
     * @param position Index in the plaintext representation.
     * @return List of entity IDs
     */
    public List<Integer> getEntityIdsAtPosition(int position) {
        List<Integer> result = new ArrayList<Integer>();
        for (EntityAnnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                result.add(anotation.getId());
            }
        }
        return result;
    }

    /**
     * Finds out which entities are annotated at specified position in the text.
     * If no entity is marked, returns empty list.
     * @param position Index in the plaintext representation.
     * @return List of entity annotations
     */
    public List<EntityAnnotation> getEntitiesAtPosition(int position) {
        List<EntityAnnotation> result = new ArrayList<EntityAnnotation>();
        for (EntityAnnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                result.add(anotation);
            }
        }
        return result;
    }
    
    /**
     * Fetches normalized names of all entity annotations from their sentence
     * trees and sets attributes in original DOM document.
     * @param normalizedNameAttributeName Name of attribute to which the
     * normalized names are saved in DOM.
     */
    public void fetchNormalizedNames(String normalizedNameAttributeName) {
        for (EntityAnnotation entity: anotations) {
            entity.fetchNormalizedName();
                        
            Element anotationElement = entity.getAnotationElement();
            
            anotationElement.setAttribute(normalizedNameAttributeName, entity.getNormalizedName());
        }
    }
    
    /**
     * Sets all entity normalized names as attributes in original DOM document.
     * Difference from fetchNormalizedNames() is that this method doesn't
     * fetch the names from SentenceTrees, so they can be set or updated
     * manually.
     * @param normalizedNameAttributeName Name of attribute to which the
     * normalized names are saved in DOM.
     */
    public void updateNormalizedNames(String normalizedNameAttributeName) {
        for (EntityAnnotation entity: anotations) {
            Element anotationElement = entity.getAnotationElement();
            anotationElement.setAttribute(normalizedNameAttributeName, entity.getNormalizedName());
        }
    }
    
    /**
     * Fetches text content of an entity with specified index
     * @param i Index (not ID!) of entity.
     * @return String with content of the entity in original text.
     */
    public String getContentForEntityAnotation(int i) {
        return originalText.substring(anotations.get(i).getBegin(), anotations.get(i).getEnd());
    }
    
    /**
     * Saves the original DOM document to an XML file.
     * @param output Target file name.
     * @throws DOMException DOM processing failed.
     */
    public void generateOutput(String output) throws DOMException {
        DOMLoader.saveDOM(output, DOMDocument);
    }
        
    /**
     * Gets list of all entities.
     * @return List of all entities.
     */
    public List<EntityAnnotation> getEntities() {
        return anotations;
    }
}
