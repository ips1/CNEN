
package cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions;

/**
 * Exception thrown when morphology loading fails.
 * @author Petr Kubat
 */
public class MorphologyLoadingException extends Exception {
    public MorphologyLoadingException() { super(); }
    public MorphologyLoadingException(String message) { super(message); }
    public MorphologyLoadingException(String message, Throwable cause) { super(message, cause); }
    public MorphologyLoadingException(Throwable cause) { super(cause); }
}
