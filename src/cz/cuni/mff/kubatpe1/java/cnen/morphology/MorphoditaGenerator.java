
package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyGeneratingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.ufal.morphodita.*;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

/**
 * MorphologyGenerator implementation which uses MorphoDiTa library and java
 * bindings.
 * @author Petr Kubat
 */
public class MorphoditaGenerator implements MorphologyGenerator {

    Morpho morphology;

    /**
     * Default constructor for MorphoditaGenerator class.
     * Requires a dictionary for MorphoDiTa library.
     * @param dictionaryFile Path to the dictionary.
     * @throws FileNotFoundException Dictionary doesn't exist.
     */
    public MorphoditaGenerator(String dictionaryFile) throws FileNotFoundException {
        // Loads the morphology from dictionary
        morphology = Morpho.load(dictionaryFile);
        if (morphology == null) {
            throw new FileNotFoundException("Dictionary file " + dictionaryFile + " doesn't exist");
        }
    }
    
    @Override
    public String generateForTag(String word, Tag targetTag) throws MorphologyGeneratingException {
        // Cleans lemma from suffixes and additional numbers
        String cleanLemma = parseLemma(word);

        TaggedLemmasForms lemmasForms = new TaggedLemmasForms();
        String tagString = targetTag.toString();
        
        // First attempt to generate
        morphology.generate(cleanLemma, tagString, 1, lemmasForms);
        
        if (lemmasForms.isEmpty()) {
            // Can't generate exact form, we try to use wildcard without variant
            tagString = targetTag.toWildcardString(false);
            lemmasForms = new TaggedLemmasForms();
            // Second attempt to generate
            morphology.generate(cleanLemma, tagString, 1, lemmasForms);
            if (lemmasForms.isEmpty()) {
                // Failure again, we use wildcard with variant
                tagString = targetTag.toWildcardString(true);
                lemmasForms = new TaggedLemmasForms();
                morphology.generate(cleanLemma, tagString, 1, lemmasForms);
                if (lemmasForms.isEmpty()) {
                    // Generation failed
                    throw new MorphologyGeneratingException("Can't generate " + tagString + " for " + cleanLemma);
                }
            }
        }
        
        // Fetching the result
        TaggedForms taggedForms = lemmasForms.get(0).getForms();
        if (lemmasForms.isEmpty()) {
            throw new MorphologyGeneratingException("Can't generate " + tagString + " for " + cleanLemma);
        }
        return (taggedForms.get(0).getForm());
        // TODO - one more check
    }
    
    /**
     * Parses the lemma and returns the raw lemma.
     * @param originalLemma Original lemma generated with morphology containing
     * additional suffixes
     * @return Clean lemma
     */
    public String parseLemma(String originalLemma) {
        // Removing suffixes
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
    
    /**
     * Finalizer for the class.
     * Cleans the MorphoDiTa supporting Morphology instance.
     * @throws Throwable Finalization fails.
     */
    @Override
    public void finalize() throws Throwable {
        try {
            morphology.delete();
        } finally {
            super.finalize();
        }
    }
    
}
