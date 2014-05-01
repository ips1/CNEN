/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.treex.exceptions;

/**
 *
 * @author petrkubat
 */
public class TreexException extends Exception {
    public TreexException() { super(); }
    public TreexException(String message) { super(message); }
    public TreexException(String message, Throwable cause) { super(message, cause); }
    public TreexException(Throwable cause) { super(cause); }   
}
