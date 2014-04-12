/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.anotations;

/**
 *
 * @author petrkubat
 */
public class AnotationParsingException extends Exception {
    public AnotationParsingException() { super(); }
    public AnotationParsingException(String message) { super(message); }
    public AnotationParsingException(String message, Throwable cause) { super(message, cause); }
    public AnotationParsingException(Throwable cause) { super(cause); }   
}
