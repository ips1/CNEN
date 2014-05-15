/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

/**
 *
 * @author petrkubat
 */
public class AnalyticalFunction {
    private String afun;
    
    public AnalyticalFunction(String afun) {
        this.afun = afun;
    }
    
    public boolean isAttribute() {
        if (afun.length() < 3) return false;
        return (afun.substring(0, 3).equals("Atr"));
    }
    
    public boolean isConcordance() {
        return (afun.indexOf("_Co") >= 0);
    }
}
