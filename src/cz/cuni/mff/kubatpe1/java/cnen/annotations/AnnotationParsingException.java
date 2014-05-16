
package cz.cuni.mff.kubatpe1.java.cnen.annotations;

/**
 * Exception thrown when the XML parsing fails.
 * @author Petr Kubat
 */
public class AnnotationParsingException extends Exception {
    public AnnotationParsingException() { super(); }
    public AnnotationParsingException(String message) { super(message); }
    public AnnotationParsingException(String message, Throwable cause) { super(message, cause); }
    public AnnotationParsingException(Throwable cause) { super(cause); }   
}
