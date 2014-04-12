/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions;

/**
 *
 * @author petrkubat
 */
public class TextMatchingException extends Exception {
    public TextMatchingException() { super(); }
    public TextMatchingException(String message) { super(message); }
    public TextMatchingException(String message, Throwable cause) { super(message, cause); }
    public TextMatchingException(Throwable cause) { super(cause); }   
}
