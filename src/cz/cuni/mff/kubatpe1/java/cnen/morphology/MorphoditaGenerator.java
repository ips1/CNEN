/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import cz.cuni.mff.ufal.morphodita.*;

/**
 *
 * @author Petr Kubat
 */
public class MorphoditaGenerator implements MorphologyGenerator {

    Morpho morphology;

    public MorphoditaGenerator(String dictionaryFile) {
        morphology = Morpho.load(dictionaryFile);
        // TODO: if (morphology == null) throw 
    }
    
    @Override
    public String generateForTag(String word, Tag targetTag) {
        String[] parts = word.split("-");
        String cleanLemma = parts[0];
        TaggedLemmasForms lemmasForms = new TaggedLemmasForms();
        String tagString = targetTag.toString();
        morphology.generate(cleanLemma, tagString, 1, lemmasForms);
        // TODO - generate error, result empty
        TaggedLemmaForms lemmaForms = lemmasForms.get(0);
        return (lemmaForms.getForms().get(0).getForm());
        // TODO - one more check
    }
    
}
