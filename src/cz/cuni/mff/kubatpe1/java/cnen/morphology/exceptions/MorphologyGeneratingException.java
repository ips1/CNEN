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
public class MorphologyGeneratingException extends Exception {
    public MorphologyGeneratingException() { super(); }
    public MorphologyGeneratingException(String message) { super(message); }
    public MorphologyGeneratingException(String message, Throwable cause) { super(message, cause); }
    public MorphologyGeneratingException(Throwable cause) { super(cause); }
}
