/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.parsing;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.*;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.FileInputStream;
import java.util.List;


/**
 *
 * @author Petr
 */
public interface SentenceTreeParser {
    List<SentenceTree> parseTree(String path) throws TreeParsingException;
    //SentenceTree parseTree(InputStream is) throws TreeParsingException;
}
