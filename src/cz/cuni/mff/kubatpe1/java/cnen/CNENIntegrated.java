/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.actions.BasicRecursiveNormalizer;
import cz.cuni.mff.kubatpe1.java.cnen.actions.TreeAction;
import cz.cuni.mff.kubatpe1.java.cnen.morphology.MorphoditaGenerator;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petrkubat
 */
public class CNENIntegrated {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setProperty("file.encoding", "utf-8");
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        
        if (args.length < 2) {
            System.err.println("Input and output files must be specified!");
            return;
        }
        
        String inputFile = args[0];
        String tmpFile = "in.txt";
        String outputFile = args[1];
        try {
            prepareTreexInput(inputFile, tmpFile);
            TreexInterface.runTreex(tmpFile, args[2], true);
            generateOutput(inputFile, tmpFile, outputFile);
        } 
        catch (Exception ex) {
            System.err.println(ex);
        }
        
    }
    
    public static void prepareTreexInput(String inputFile, String preparedFile) throws FileNotFoundException, UnsupportedEncodingException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
        PrintStream out = new PrintStream(new FileOutputStream(preparedFile), true, "UTF8");
                
        String line;
        int lineNo = 0;
        int entityNo = 0;
        try {
            while ((line = in.readLine()) != null) {
                if (lineNo % 4 == 1) {
                    out.println(line);
                }
                
                lineNo++;
            }
        } 
        catch (IOException ex) {
            System.err.println("Error reading input: " + ex);
        } 
        finally {
            try {
                in.close();
                out.close();
            } 
            catch (IOException ex) {
                System.err.println("Error: File can't be closed");
                System.exit(1);
            }
        }
    }
    
    public static void generateOutput(String inputFile, String resultBaseName, String outputFile) throws FileNotFoundException, TreeParsingException, UnsupportedEncodingException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
        PrintStream out = new PrintStream(new FileOutputStream(outputFile), true, "UTF8");
        
        String base = TreexInterface.removeSuffix(resultBaseName);
        
        TreeAction act = new BasicRecursiveNormalizer(false, -1, new MorphoditaGenerator("czech-morfflex-131112.dict"));

        
        String line;
        int lineNo = 0;
        int entityNo = 1;
        try {
            while ((line = in.readLine()) != null) {
                switch (lineNo % 4) {
                    case 0:
                        out.println(line);
                        break;
                    case 1:
                        System.err.println("Normalizing entity: " + line);
                        String fname = base + String.format("%03d.treex", entityNo);
                        entityNo++;
                        String result = CNEN.normalize(fname, act);
                        System.err.println("Normalization finished!");
                        File f = new File(fname);
                        f.delete();
                        out.println(line);
                        out.println(result);
                        out.println();
                        break;
                }
                
                lineNo++;
            }
        } 
        catch (IOException ex) {
            System.err.println("Error reading input: " + ex);
        } 
        finally {
            try {
                in.close();
                out.close();
            } 
            catch (IOException ex) {
                System.err.println("Error: File can't be closed");
                System.exit(1);
            }
        }
    }
}
