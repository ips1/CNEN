
package cz.cuni.mff.kubatpe1.java.cnen.sentencetree;

/**
 * Class representing an analytical function of a node in a-layer dependency
 * tree of a Czech sentence.
 * In this project, it is used as a part of a SentenceTree, which is dependency
 * tree implementation.
 * @author Petr Kubat
 */
public class AnalyticalFunction {
    // Textual representation of the function
    private String afun;
    // Indicates coordination with another node
    private boolean coordination;
    
    /**
     * Default constructor for AnalyticalFunction class.
     * @param afun String representing the function according to PDT.
     * @param coordination Boolean determining whether the function is in 
     * coordination.
     */
    public AnalyticalFunction(String afun, boolean coordination) {
        this.afun = afun;
        this.coordination = coordination;
    }
    
    /**
     * Gets the textual representation of the function according to PDT.
     * @return String containing the function.
     */
    public String getAfun() {
        return this.afun;
    }
    
    /**
     * Finds out whether the function is attribute.
     * @return True if the function is attribute, false otherwise.
     */
    public boolean isAttribute() {
        if (afun.length() < 3) return false;
        return (afun.substring(0, 3).equals("Atr"));
    }
    
    /**
     * Finds out whether the function contains coordination.
     * @return True if the function contains coordination, false otherwise.
     */
    public boolean isCoordination() {
        return coordination;
    }
}
