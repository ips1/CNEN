
package cz.cuni.mff.kubatpe1.java.cnen.actions;

/**
 * Class for matching the case of two strings.
 * @author Petr Kubat
 */
public class CaseMatcher {
    /**
     * Matches the case of the target string with the source string.
     * Modifies only the target string. Matches following situations:
     * - everything lowercase
     * - everything uppercase
     * - first letter uppercase
     * @param source String serving as a source of the case pattern
     * @param target String to have its case changed
     */
    public String matchCase(String source, String target) {
        if (source == null || source.length() < 1) return target;
        
        boolean allUpper = true;
        boolean allLower = true;
        for (int i = 0; i < source.length(); i++) {
            if (Character.isLowerCase(source.charAt(i))) {
                allUpper = false;                
            }
            if (Character.isUpperCase(source.charAt(i))) {
                allLower = false;
            }
        }
        
        if (allUpper) {
            return target.toUpperCase();
        }
        
        if (allLower) {
            return target.toLowerCase();
        }
        
        if (Character.isUpperCase(source.charAt(0))) {
            StringBuilder sb = new StringBuilder(target);
            sb.setCharAt(0, Character.toUpperCase(target.charAt(0)));
            return sb.toString();
        }
        
        return target;
    }

}