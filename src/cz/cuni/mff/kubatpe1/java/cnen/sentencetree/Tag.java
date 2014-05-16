
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.InvalidTagException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a Czech morphological tag according to PDT.
 * The tag is mutable and the positional values are not validated.
 * @author Petr Kubat
 */
public class Tag implements Cloneable {
    
    // Number of position in the tag
    private static final int count = 15;

    // Positional character values
    public char wordClass;
    public char wordSubClass;
    public char gender;
    public char number;
    public char grCase;
    public char possesiveGender;
    public char possesiveNumber;
    public char person;
    public char tense;
    public char degree;
    public char negation;
    public char activity;
    public char variant;
    
    // Constructor which parses the tag from string representation
    /**
     * Default constructor for Tag class.
     * Parses the tag from a string representation.
     * @param str String representing a PDT tag.
     * @throws InvalidTagException Presented String doesn't represent a correct
     * PDT tag.
     */
    public Tag(String str) throws InvalidTagException {
        setToString(str);
    }
    
    /**
     * Sets the tag to values parsed from String representation.
     * @param str String representing a PDT tag.
     * @throws InvalidTagException Presented String doesn't represent a correct
     * PDT tag.
     */
    public final void setToString(String str) throws InvalidTagException {
        // String must have specific length
        if (str.length() != count) {
            throw new InvalidTagException();
        }
        
        wordClass = str.charAt(0);
        wordSubClass = str.charAt(1);
        gender = str.charAt(2);
        number = str.charAt(3);
        grCase = str.charAt(4);
        possesiveGender = str.charAt(5);
        possesiveNumber = str.charAt(6);
        person = str.charAt(7);
        tense = str.charAt(8);
        degree = str.charAt(9);
        negation = str.charAt(10);
        activity = str.charAt(11);
        variant = str.charAt(14);
        
    }
    
    /**
     * Generates a String representation of the tag.
     * @return PDT String representation of the tag.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(wordClass);
        sb.append(wordSubClass);
        sb.append(gender);
        sb.append(number);
        sb.append(grCase);
        sb.append(possesiveGender);
        sb.append(possesiveNumber);
        sb.append(person);
        sb.append(tense);
        sb.append(degree);
        sb.append(negation);
        sb.append(activity);
        sb.append('-');
        sb.append('-');
        sb.append(variant);

        return sb.toString();
    }
    
    /**
     * Generates a String representation of the tag with MorphoDiTa wildcards.
     * Undefined values ("-") are replaced with MorphoDiTa wildcards ("?").
     * @param wildcardVariant If true, the variant value is also set to 
     * wildcard, otherwise, it is left as undefined.
     * @return Wildcard String representation of the tag.
     */
    public String toWildcardString(boolean wildcardVariant) {
        StringBuilder sb = new StringBuilder();
        sb.append(wordClass == '-' ? '?' : wordClass);
        sb.append(wordSubClass == '-' ? '?' : wordSubClass);
        sb.append(gender == '-' ? '?' : gender);
        sb.append(number == '-' ? '?' : number);
        sb.append(grCase == '-' ? '?' : grCase);
        sb.append(possesiveGender == '-' ? '?' : possesiveGender);
        sb.append(possesiveNumber == '-' ? '?' : possesiveNumber);
        sb.append(person == '-' ? '?' : person);
        sb.append(tense == '-' ? '?' : tense);
        sb.append(degree == '-' ? '?' : degree);
        sb.append(negation == '-' ? '?' : negation);
        sb.append(activity == '-' ? '?' : activity);
        sb.append('?');
        sb.append('?');
        if (wildcardVariant) {
            sb.append('?');
        }
        else {
            sb.append(variant == '-' ? '?' : variant);
        }

        return sb.toString();
    }
    
    /**
     * Finds out whether the tag represents conjunction.
     * @return True if the tag represents conjunction.
     */
    public boolean isConjunction() {
        return wordClass == 'J';
    }
    
    /**
     * Finds out whether the tag represents verb.
     * @return True if the tag represents verb.
     */
    public boolean isVerb() {
        return wordClass == 'V';
    }
    
    /**
     * Finds out whether the tag represents adjective.
     * @return True if the tag represents adjective.
     */
    public boolean isAdjective() {
        return wordClass == 'A';
    }
    
    /**
     * Finds out whether the tag represents pronoun.
     * @return True if the tag represents pronoun.
     */
    public boolean isPronoun() {
        return wordClass == 'P';
    }
    
    /**
     * Finds out whether the tag represents numeral.
     * @return True if the tag represents numeral.
     */
    public boolean isNumeral() {
        return wordClass == 'N';
    }
    
    /**
     * Finds out whether the tag represents punctuation.
     * @return True if the tag represents punctuation.
     */
    public boolean isPunctuation() {
        return wordClass == 'Z';
    }
    
    /**
     * Finds out whether the tag represents preposition.
     * @return True if the tag represents preposition.
     */
    public boolean isPreposition() {
        return wordClass == 'R';
    }
    
    /**
     * Finds out whether the tag represents noun.
     * @return True if the tag represents noun.
     */
    public boolean isNoun() {
        return wordClass == 'N';
    }
    
    /**
     * Finds out whether the tag represents singular form.
     * @return True if the tag represents singular form.
     */
    public boolean isSingular() {
        return number == 'S';
    }
    
    /**
     * Finds out whether the tag represents plural form.
     * @return True if the tag represents plural form.
     */
    public boolean isPlural() {
        return number == 'P';
    }
    
    /**
     * Finds out whether the tag matches other tag in number.
     * @param otherTag Tag to be matched with.
     * @return True if the tags match. 
     */
    public boolean matchesNumber(Tag otherTag) {
        return this.number == otherTag.number;
    }
    
    /**
     * Finds out whether the tag matches other tag in gramatical case.
     * @param otherTag Tag to be matched with.
     * @return True if the tags match. 
     */
    public boolean matchesGrCase(Tag otherTag) {
        return this.grCase == otherTag.grCase;
    }
    
    /**
     * Finds out whether the tag matches other tag in gender.
     * @param otherTag Tag to be matched with.
     * @return True if the tags match. 
     */
    public boolean matchesGender(Tag otherTag) {
        return this.gender == otherTag.gender;
    }
    
    /**
     * Matches the tag to another in matter of grammatical case.
     * @param source Source of the number, case and gender
     */
    public void matchWithTag(Tag source) {
        if (source.grCase != 'X' && source.grCase != '-') {
            this.grCase = source.grCase;
        }
    }
    
    /**
     * Makes a copy of the tag.
     * Uses default clone() method.
     * @return New tag containing the same values.
     */
    public Tag getCopy() {
        try {
            return (Tag)this.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }   
    
}
