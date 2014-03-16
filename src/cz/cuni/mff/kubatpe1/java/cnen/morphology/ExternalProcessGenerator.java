/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.morphology.exceptions.MorphologyLoadingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petrkubat
 */
public class ExternalProcessGenerator implements MorphologyGenerator {

    String path;
    Process process;
    BufferedReader processOutput;
    PrintWriter processInput;
    
    public ExternalProcessGenerator(String path, String dictPath) throws MorphologyLoadingException {
        this.path = path;
        // TODO: Exception handling
        try {
            this.process = new ProcessBuilder(path, dictPath, "1").start();          
        } catch (IOException ex) {
            throw new MorphologyLoadingException(ex);
        }
        
        processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        processInput = new PrintWriter(process.getOutputStream());
    }
    
    
    @Override
    public String generateForTag(String word, Tag targetTag) {
        processInput.printf("%s\t%s\n", word, targetTag.toString());
        processInput.flush();
        String out;
        try {
             out = processOutput.readLine();
        } catch (IOException ex) {
            // TODO: throw new MorphologyGeneratingException();
            return "";            
        }
        if (out == null) {
            // TODO: throw new MorphologyGeneratingException();
            return "";
        }
        
        String[] outParts = out.split("\t");
        return outParts[0];
    }
    
}
