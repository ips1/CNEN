/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.actions;

/**
 * Exception signaling an error while performing a TreeAction on a SentenceTree
 * @author Petr Kubat
 */
public class TreeActionException extends Exception {
    public TreeActionException() { super(); }
    public TreeActionException(String message) { super(message); }
    public TreeActionException(String message, Throwable cause) { super(message, cause); }
    public TreeActionException(Throwable cause) { super(cause); }
}
