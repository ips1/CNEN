
package cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions;

/**
 * Exception thrown when the generation fails.
 * @author Petr Kubat
 */
public class MorphologyGeneratingException extends Exception {
    public MorphologyGeneratingException() { super(); }
    public MorphologyGeneratingException(String message) { super(message); }
    public MorphologyGeneratingException(String message, Throwable cause) { super(message, cause); }
    public MorphologyGeneratingException(Throwable cause) { super(cause); }
}
