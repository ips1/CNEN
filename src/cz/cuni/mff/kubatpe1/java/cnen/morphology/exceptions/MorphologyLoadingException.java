/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions;

/**
 *
 * @author petrkubat
 */
public class MorphologyLoadingException extends Exception {
    public MorphologyLoadingException() { super(); }
    public MorphologyLoadingException(String message) { super(message); }
    public MorphologyLoadingException(String message, Throwable cause) { super(message, cause); }
    public MorphologyLoadingException(Throwable cause) { super(cause); }
}
