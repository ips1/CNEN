/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions;

/**
 *
 * @author Petr
 */
public class TreeParsingException extends Exception {
    public TreeParsingException() { super(); }
    public TreeParsingException(String message) { super(message); }
    public TreeParsingException(String message, Throwable cause) { super(message, cause); }
    public TreeParsingException(Throwable cause) { super(cause); }
    
}
