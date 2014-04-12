/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.anotations;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author petrkubat
 */
public class AnotatedText {
    private String originalText;
    private List<EntityAnotation> anotations;
    
    private AnotatedText(String originalText, List<EntityAnotation> anotations) {
        this.originalText = originalText;
        this.anotations = anotations;
    }
    
    public static AnotatedText parseText(String text, String entityTag) throws AnotationParsingException {
        List<EntityAnotation> anotations = new ArrayList<EntityAnotation>();
        StringBuilder originalText = new StringBuilder();
        
        String startSeq = "<" + entityTag;
        String endSeq = "</" + entityTag + ">";
        
        int currentId = 0;
        
        int i = 0;
	int start = 0;
	int relPosition = 0;
        
        while ((i = text.indexOf(startSeq, start)) != -1) {
            // Starting tag sequence has been matched
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
    
    public String getText() {
        return originalText;
    }
    
    // Returns -1 if there is no entity
    public int getEntityIdAtPosition(int position) {
        for (EntityAnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                return anotation.getId();
            }
        }
        return -1;
    }

    public EntityAnotation getEntityAtPosition(int position) {
        for (EntityAnotation anotation: anotations) {
            if (position >= anotation.getBegin() && position < anotation.getEnd()) {
                return anotation;
            }
        }
        return null;
    }
    
    public String generateNormalizedOutput(String entityTag, String normalizedNameAttributeName) {
        StringBuilder output = new StringBuilder();
        
        int lastIndex = 0;
        for (int i = 0; i < anotations.size(); i++) {
            EntityAnotation currentAnotation = anotations.get(i);
            
            output.append(originalText.substring(lastIndex, currentAnotation.getBegin()));
            
            output.append("<").append(entityTag);
            output.append(currentAnotation.getAttributes());
            output.append(" ").append(normalizedNameAttributeName).append("=\"");
            output.append(currentAnotation.fetchNormalizedName()).append("\">");
            
            output.append(originalText.substring(currentAnotation.getBegin(), currentAnotation.getEnd()));
            
            output.append("</").append(entityTag).append(">");
            
            lastIndex = currentAnotation.getEnd();
        }
        
        output.append(originalText.substring(lastIndex));
        
        return output.toString();
    }
}
