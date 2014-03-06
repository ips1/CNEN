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
public class InvalidTagException extends Exception {
    public InvalidTagException() { super(); }
    public InvalidTagException(String message) { super(message); }
    public InvalidTagException(String message, Throwable cause) { super(message, cause); }
    public InvalidTagException(Throwable cause) { super(cause); }   
}
