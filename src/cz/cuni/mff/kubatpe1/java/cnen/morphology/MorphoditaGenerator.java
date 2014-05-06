/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyGeneratingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.ufal.morphodita.*;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

/**
 *
 * @author Petr Kubat
 */
public class MorphoditaGenerator implements MorphologyGenerator {

    Morpho morphology;

    public MorphoditaGenerator(String dictionaryFile) throws FileNotFoundException {
        morphology = Morpho.load(dictionaryFile);
        if (morphology == null) {
            throw new FileNotFoundException("Dictionary file " + dictionaryFile + " doesn't exist");
        }
    }
    
    @Override
    public String generateForTag(String word, Tag targetTag) throws MorphologyGeneratingException {
        String cleanLemma = parseLemma(word);

        TaggedLemmasForms lemmasForms = new TaggedLemmasForms();
        String tagString = targetTag.toString();
        morphology.generate(cleanLemma, tagString, 1, lemmasForms);
        
        if (lemmasForms.isEmpty()) {
            // Can't generate exact form, we try to use wildcard
            tagString = targetTag.toWildcardString();
            lemmasForms = new TaggedLemmasForms();
            morphology.generate(cleanLemma, tagString, 1, lemmasForms);
            if (lemmasForms.isEmpty()) {
                // Can't generate even for wildcard - throwing exception
                throw new MorphologyGeneratingException("Can't generate " + tagString + " for " + cleanLemma);
            }
        }
        TaggedForms taggedForms = lemmasForms.get(0).getForms();
        if (lemmasForms.isEmpty()) {
            throw new MorphologyGeneratingException("Can't generate " + tagString + " for " + cleanLemma);
        }
        return (taggedForms.get(0).getForm());
        // TODO - one more check
    }
    
    /**
     * Parses the lemma and returns the raw lemma.
     * @param originalLemma Original lemma generated with morphology containing additional tags
     * @return Clean lemma
     */
    public String parseLemma(String originalLemma) {
        String[] additionSeparators = {"_:", "_;", "_,", "_^"};
        
        String currentString = originalLemma;
        
        for (String s: additionSeparators) {
            String[] parts = currentString.split(Pattern.quote(s));
            currentString = parts[0];
        }
        
        String[] finalParts = currentString.split("-");
        
        // String was only the "-"
        if (finalParts.length < 1) {
            return currentString;
        }
        
        try {
            Integer.parseInt(finalParts[finalParts.length - 1]);
            // Integer parsing was succesful - last part is not part of the raw lemma
            StringBuilder sb = new StringBuilder(finalParts[0]);
            for (int i = 1; i < finalParts.length - 1; i++) {
                sb.append("-");
                sb.append(finalParts[i]);
            }
            currentString = sb.toString();
        }
        catch (NumberFormatException ex) {
            
        }
        
        return currentString;
    }
    
    @Override
    public void finalize() {
        morphology.delete();
    }
    
}
