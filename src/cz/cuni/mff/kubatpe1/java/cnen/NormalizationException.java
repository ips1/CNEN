/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

/**
 * Exception signaling an error during a normalization procedure
 * @author petrkubat
 */
public class NormalizationException extends Exception {
    public NormalizationException() { super(); }
    public NormalizationException(String message) { super(message); }
    public NormalizationException(String message, Throwable cause) { super(message, cause); }
    public NormalizationException(Throwable cause) { super(cause); }
}
