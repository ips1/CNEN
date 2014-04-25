/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.tools;

import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.EntityAnotation;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tool for changing the format of normalized documents
 * Takes input and output file names as arguments
 * @author Petr Kubat
 */
public class AnotationFormatter {

    private static final String entityTag = "ne";
    private static final String normalizedNameAttribute = "normalized_name";
    private static final String identityMark = "***";
    
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Input and output files must be specified!");
            return;
        }
        
        String inFile = args[0];
        String outFile = args[1];
        try {
            processFile(inFile, outFile);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AnotationParsingException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void processFile(String inFile, String outFile) throws FileNotFoundException, UnsupportedEncodingException, IOException, AnotationParsingException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF8"));
        PrintStream out = new PrintStream(new FileOutputStream(outFile), true, "UTF8");

        String line = in.readLine();
        
        while (line != null) {
            String nextLine = in.readLine();
            String[] anotations;
            if (nextLine != null && !nextLine.equals("") && nextLine.charAt(0) == '#') {
                anotations = nextLine.substring(1).split("; *");
                nextLine = in.readLine();
            }
            else {
                // empty array for the line
                anotations = new String[0];
            }
            
            out.println(processLine(line, anotations));
            
            line = nextLine;
        }
    }
            
    public static String processLine(String line, String[] anotations) throws AnotationParsingException {
        AnotatedText anotatedLine = AnotatedText.parseText(line, entityTag);
        
        List<EntityAnotation> entities = anotatedLine.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            if (i < anotations.length) {
                if (anotations[i].equals(identityMark)) {
                    entities.get(i).setNormalizedName(anotatedLine.getContentForEntityAnotation(i));
                }
                else {
                    entities.get(i).setNormalizedName(anotations[i]);
                }
            }
        }
        
        return anotatedLine.generateNormalizedOutput("ne", "normalized_name");
    }
}
