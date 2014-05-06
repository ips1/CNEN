/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

/**
 *
 * @author petrkubat
 */
public class WordForm {
    public String content;
    public String lemma;
    public Tag tag;
    public boolean normalized;
    
    public WordForm(String content, String lemma, Tag tag) {
        this.content = content;
        this.lemma = lemma;
        this.tag = tag;
        this.normalized = false;
    }
    
    public WordForm getCopy() {
        return new WordForm(content, lemma, tag.getCopy());
    }
}
