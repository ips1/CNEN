/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.dom;

/**
 *
 * @author petrkubat
 */
public class DOMException extends Exception {
    public DOMException() { super(); }
    public DOMException(String message) { super(message); }
    public DOMException(String message, Throwable cause) { super(message, cause); }
    public DOMException(Throwable cause) { super(cause); }
    
}