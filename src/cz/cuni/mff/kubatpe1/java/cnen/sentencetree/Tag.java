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

    private char wordClass;
    private char wordSubClass;
    private char gender;
    private char number;
    private char grCase;
    private char possesiveGender;
    private char possesiveNumber;
    private char person;
    private char tense;
    private char degree;
    private char negation;
    private char activity;
    private char variant;
    
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
    
}
