/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.*;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.FileInputStream;


/**
 *
 * @author Petr
 */
public interface SentenceTreeParser {
    SentenceTree parseTree(String path) throws TreeParsingException;
}
