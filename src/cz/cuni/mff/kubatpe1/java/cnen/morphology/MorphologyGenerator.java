
package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyGeneratingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;

/**
 * Interface for classes performing the morpohological generation.
 * @author Petr Kubat
 */
public interface MorphologyGenerator {
    
    /**
     * Generates a word form for specified lemma and tag.
     * @param word Lemma for which we generate.
     * @param targetTag Tag for which we generate.
     * @return Form generated from lemma and tag.
     * @throws MorphologyGeneratingException Generation failed.
     */
    public String generateForTag(String word, Tag targetTag) throws MorphologyGeneratingException;
}
