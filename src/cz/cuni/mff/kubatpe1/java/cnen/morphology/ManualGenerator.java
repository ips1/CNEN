
package cz.cuni.mff.kubatpe1.java.cnen.morphology;

import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.Tag;
import java.util.Scanner;

/**
 * Manual MorphologyGenerator.
 * Uses stdout and stdin to ask for generating forms.
 * @author Petr Kubat
 */
public class ManualGenerator implements MorphologyGenerator {

    @Override
    public String generateForTag(String word, Tag targetTag) {
        System.out.println("Morphology generation required:");
        System.out.println("Lemma: " + word);
        System.out.println("Required tag: " + targetTag);
        
        Scanner s = new Scanner(System.in);

        String result = s.nextLine();
        return result;
    }
    
}
