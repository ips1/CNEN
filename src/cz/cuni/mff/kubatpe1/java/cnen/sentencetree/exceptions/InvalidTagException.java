
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions;

/**
 * Exception thrown when the tag is not valid according to PDT.
 * @author Petr Kubat
 */
public class InvalidTagException extends Exception {
    public InvalidTagException() { super(); }
    public InvalidTagException(String message) { super(message); }
    public InvalidTagException(String message, Throwable cause) { super(message, cause); }
    public InvalidTagException(Throwable cause) { super(cause); }   
}
