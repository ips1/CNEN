/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.InvalidTagException;

/**
 *
 * @author Petr
 */
public class Tag {
    
    private static final int count = 15;

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
    public Tag(String str) throws InvalidTagException {
        setToString(str);
    }
    
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
    
    public boolean isConjunction() {
        return wordClass == 'J';
    }
    
    public boolean isVerb() {
        return wordClass == 'V';
    }
    
    public boolean isAdjective() {
        return wordClass == 'A';
    }
    
    public boolean isPunctuation() {
        return wordClass == 'Z';
    }
    
    public boolean isSingular() {
        return number == 'S';
    }
    
    public boolean isPlural() {
        return number == 'P';
    }
    
    
}
