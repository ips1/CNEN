/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.treex;

import cz.cuni.mff.kubatpe1.java.cnen.treex.exceptions.TreexException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for running the Treex external program and fetching its results.
 * @author petrkubat
 */
public class TreexInterface {
    
    /**
     * Runs a Treex instance with specified parameters
     * @param inputFile Input file for the Treex
     * @param scenario Scenario without the reader and writer blocks
     * @param oneDocPerLine Specifies whether each line should be considered a new document
     * @return List of result file names - if oneDocPerLine is true, more than one file name might be returned
     * @throws TreexException Error when running the Treex process
     */
    public static List<String> runTreex(String inputFile, String scenario, boolean oneDocPerLine) throws TreexException {
        String docPerLine = oneDocPerLine ? "lines_per_doc=1 " : "";
        String command = "treex Util::SetGlobal language=cs Read::Text " + docPerLine + "from=" + inputFile + " " + scenario + " Write::Treex to=. compress=0";
        System.out.println("Running treex...");
        
        int result = 1;
        String line;
        // Variables for storing last two lines of treex stderr
        String last = null;
        String previous = null;
        
        try {
            Process treexProcess = Runtime.getRuntime().exec(command);

            BufferedReader errReader = new BufferedReader(new InputStreamReader(treexProcess.getErrorStream()));
            
            while ((line = errReader.readLine()) != null) {
                previous = last;
                last = line;
                System.err.println(line);
            }

            result = treexProcess.waitFor();
        }
        catch (IOException ex) {
            throw new TreexException("Error while communicating with the process", ex);
        } 
        catch (InterruptedException ex) {
            throw new TreexException("Error - treex process interrupted", ex);
        }
        
        // Returning result filenames
        List<String> results = new ArrayList<String>();
        
        if (oneDocPerLine) {
            if (previous == null) throw new TreexException("Wrong treex output!");
            // We have to find out how many result files there are
            String[] parts = previous.split("\t");
            if (parts.length < 2) throw new TreexException("Wrong treex output!");

            String[] words = parts[1].split(" ");
            if (words.length < 2) throw new TreexException("Wrong treex output!");
            
            int resultNumber = Integer.parseInt(words[1]);
            
            String base = removeSuffix(inputFile);
            
            for (int i = 0; i < resultNumber; i++) {
                results.add(base + String.format("%03d.treex", i));
            }
            
        }
        else {
            results.add(removeSuffix(inputFile) + ".treex");

        }
        
        
        return results;
    }
    
    public static String removeSuffix(String source) {
        return source.substring(0, source.lastIndexOf('.'));
    }
}
