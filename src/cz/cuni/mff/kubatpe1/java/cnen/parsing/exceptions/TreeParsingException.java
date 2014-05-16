
package cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions;

/**
 * Exception thrown when Treex format parsing failed.
 * @author Petr
 */
public class TreeParsingException extends Exception {
    public TreeParsingException() { super(); }
    public TreeParsingException(String message) { super(message); }
    public TreeParsingException(String message, Throwable cause) { super(message, cause); }
    public TreeParsingException(Throwable cause) { super(cause); }
    
}
