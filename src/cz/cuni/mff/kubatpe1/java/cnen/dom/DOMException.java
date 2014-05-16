
package cz.cuni.mff.kubatpe1.java.cnen.dom;

/**
 * Exception thrown when DOM loading from XML or saving to XML fails.
 * @author petrkubat
 */
public class DOMException extends Exception {
    public DOMException() { super(); }
    public DOMException(String message) { super(message); }
    public DOMException(String message, Throwable cause) { super(message, cause); }
    public DOMException(Throwable cause) { super(cause); }
    
}