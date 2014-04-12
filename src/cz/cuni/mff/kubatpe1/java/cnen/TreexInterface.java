/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author petrkubat
 */
public class TreexInterface {
    public static String runTreex(String inputFile, String scenario, boolean oneDocPerLine) throws IOException, InterruptedException {
        String docPerLine = oneDocPerLine ? "lines_per_doc=1 " : "";
        String command = "treex Util::SetGlobal language=cs Read::Text " + docPerLine + "from=" + inputFile + " " + scenario + " Write::Treex to=. compress=0";
        System.out.println("Running treex...");
        Process treexProcess = Runtime.getRuntime().exec(command);

        BufferedReader errReader = new BufferedReader(new InputStreamReader(treexProcess.getErrorStream()));
        String line;
        while ((line = errReader.readLine()) != null) {
            System.err.println(line);
        }
        
        int result = treexProcess.waitFor();

        // TODO sanitize this, probably exception would be best
        if (result != 0) {
            System.err.println("Treex ended with error!");
            System.exit(1);
        }
        
        // Returning result filename
        String resultFileName = removeSuffix(inputFile) + ".treex";
        
        return resultFileName;
    }
    
    public static String removeSuffix(String source) {
        return source.substring(0, source.lastIndexOf('.'));
    }
}
