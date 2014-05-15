/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.tools;

import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedTextParser;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.EntityAnotation;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMException;
import cz.cuni.mff.kubatpe1.java.cnen.dom.DOMLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tool for changing the format of normalized documents
 * Takes input and output file names as arguments
 * @author Petr Kubat
 */
public class AnotationFormatter {

    private static final String entityTag = "ne";
    private static final String normalizedNameAttribute = "normalized_name";
    private static final String identityMark = "***";
    
    private int currentEntity;
    private List<String> normalizedNames;
    
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Input and output files must be specified!");
            return;
        }
        
        AnotationFormatter formatter = new AnotationFormatter();
        
        String inFile = args[0];
        String outFile = args[1];
        try {
            formatter.processFile(inFile, outFile);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AnotationParsingException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DOMException ex) {
            Logger.getLogger(AnotationFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void processFile(String inFile, String outFile) throws FileNotFoundException, UnsupportedEncodingException, IOException, AnotationParsingException, DOMException {
        // Temporary file for Treex input
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("CNEN", null);
            tmpFile.deleteOnExit();
        } 
        catch (IOException ex) {
            System.err.println("Can't create temporary file!");
            return;
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF8"));
        PrintStream out = new PrintStream(new FileOutputStream(tmpFile), true, "UTF8");

        normalizedNames = new ArrayList<String>();
        
        out.print("<doc>");
        
        String line = in.readLine();
                
        while (line != null) {
            String[] anotations;
            if (!line.equals("") && line.charAt(0) == '#') {
                anotations = line.substring(1).split("; *");
                if (anotations.length > 1 || (anotations[0] != null && anotations[0].length() > 0)) {
                    normalizedNames.addAll(Arrays.asList(anotations));
                }
            }
            else {
                // empty array for the line
                out.println(line);
            }
                        
            line = in.readLine();
        }
        
        out.print("</doc>");
        
        Document doc = DOMLoader.loadDOM(tmpFile.getAbsolutePath());
        
        Element root = doc.getDocumentElement();
     
        currentEntity = 0;
        
        processNode(root);
        
        DOMLoader.saveDOM(outFile, doc);
    }

    private void processNode(Node node) {
         if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element)node;
            if (elem.getTagName().equals(entityTag)) {
                // Inside an entity anotation                
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    processNode(children.item(i));
                }
                String entityText;
                if (currentEntity < normalizedNames.size()) {
                    entityText = normalizedNames.get(currentEntity++);
                }
                else {
                    entityText = "";
                    System.err.println("Not enough entities!");
                }                
                elem.setAttribute(normalizedNameAttribute, entityText.equals(identityMark) ? getNodeText(elem) : entityText);
            }
            else {
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    processNode(children.item(i));
                }
            }
        }
        else {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                processNode(children.item(i));
            }
        }
    }
    
    private String getNodeText(Node node) {
        StringBuilder text = new StringBuilder();
        if (node.getNodeType() == Node.TEXT_NODE) {
            text.append(node.getTextContent());
        }
        else {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                text.append(getNodeText(children.item(i)));
            }
        }
        
        return text.toString();
    }
}
