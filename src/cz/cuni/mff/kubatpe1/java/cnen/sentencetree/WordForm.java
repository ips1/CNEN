
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

/**
 * Class representing one form of a word.
 * Servers basically as a structure grouping several attributes.
 * @author Petr Kubat
 */
public class WordForm {
    public String content;
    public String lemma;
    public Tag tag;
    public boolean normalized;
    
    /**
     * Default constructor for the WordForm class.
     * @param content Word form textual content.
     * @param lemma Word form lemma.
     * @param tag Word form tag.
     */
    public WordForm(String content, String lemma, Tag tag) {
        this.content = content;
        this.lemma = lemma;
        this.tag = tag;
        this.normalized = false;
    }
    
    /**
     * Creates a duplicate word form with the same content.
     * @return New WordForm with the same values.
     */
    public WordForm getCopy() {
        return new WordForm(content, lemma, tag.getCopy());
    }
}
