
package cz.cuni.mff.kubatpe1.java.cnen;

/**
 * Exception signaling an error during a normalization procedure
 * @author Petr Kubat
 */
public class NormalizationException extends Exception {
    public NormalizationException() { super(); }
    public NormalizationException(String message) { super(message); }
    public NormalizationException(String message, Throwable cause) { super(message, cause); }
    public NormalizationException(Throwable cause) { super(cause); }
}
