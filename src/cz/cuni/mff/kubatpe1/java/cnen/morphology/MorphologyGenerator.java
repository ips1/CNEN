/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;

/**
 *
 * @author Petr
 */
public interface MorphologyGenerator {
    public String generateForTag(String word, Tag targetTag);
}
