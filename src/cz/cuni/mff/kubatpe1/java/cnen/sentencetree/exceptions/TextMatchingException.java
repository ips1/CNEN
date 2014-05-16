
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions;

/**
 * Exception thrown when the SentenceCollection and AnnotatedText do not match.
 * @author Petr Kubat
 */
public class TextMatchingException extends Exception {
    public TextMatchingException() { super(); }
    public TextMatchingException(String message) { super(message); }
    public TextMatchingException(String message, Throwable cause) { super(message, cause); }
    public TextMatchingException(Throwable cause) { super(cause); }   
}
