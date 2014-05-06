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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author petrkubat
 */
public class AnotatedText {
    private final Document DOMDocument;
    private final String originalText;
    private final List<EntityAnotation> anotations;
    
    public AnotatedText(Document DOMDocument, String originalText, List<EntityAnotation> anotations) {
        this.DOMDocument = DOMDocument;
        this.originalText = originalText;
        this.anotations = anotations;
    }
    
    
    /*
    public static AnotatedText parseText(String text, String entityTag) throws AnotationParsingException {
        List<EntityAnotation> anotations = new ArrayList<EntityAnotation>();
        StringBuilder originalText = new StringBuilder();
        
        String startSeq = entityTag;
        String endSeq = "/" + entityTag + ">";
        
        int currentId = 0;
        
        int i = 0;
	int start = 0;
	int relPosition = 0;
        
        while ((i = text.indexOf(startSeq, start)) != -1) {
            // Starting or ending tag has been matched
            int tagEndPos = text.indexOf(">", i + startSeq.length());
            if (tagEndPos == -1) {
                throw new AnotationParsingException("Tag not ended!");
            }
            
            String attributes = text.substring(i + startSeq.length(), tagEndPos);
            
            originalText.append(text.substring(start, i));
            start = tagEndPos + 1;
            relPosition += tagEndPos + 1 - i;
            
            int j = text.indexOf(endSeq, start);
            if (j == -1) {
                j = text.length();
            }
            
            EntityAnotation entity = new EntityAnotation(start - relPosition, j - relPosition, currentId++, attributes);
            
            anotations.add(entity);
            
            originalText.append(text.substring(start, j));
            
            start = j + endSeq.length();
            relPosition += endSeq.length();
        }
        
        originalText.append(text.substring(start));
        
        AnotatedText result = new AnotatedText(originalText.toString(), anotations);
        
        return result;
    }
    */
    
    public String getText() {
        return originalText;
    }
    
    // Returns empty list
    public List<Integer> getEntityIdsAtPosition(int position) {
        List<Integer> result = new ArrayList<Integer>();
        for (EntityAnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                result.add(anotation.getId());
            }
        }
        return result;
    }

    public List<EntityAnotation> getEntitiesAtPosition(int position) {
        List<EntityAnotation> result = new ArrayList<EntityAnotation>();
        for (EntityAnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                result.add(anotation);
            }
        }
        return result;
    }
    
    public void fetchNormalizedNames(String normalizedNameAttributeName) {
        for (EntityAnotation entity: anotations) {
            entity.fetchNormalizedName();
                        
            Element anotationElement = entity.getAnotationElement();
            
            anotationElement.setAttribute(normalizedNameAttributeName, entity.getNormalizedName());
        }
    }
    
    public String getContentForEntityAnotation(int i) {
        return originalText.substring(anotations.get(i).getBegin(), anotations.get(i).getEnd());
    }
    
    public void generateNormalizedOutput(String output) throws DOMException {
        DOMLoader.saveDOM(output, DOMDocument);
    }
        
    public List<EntityAnotation> getEntities() {
        return anotations;
    }
}
