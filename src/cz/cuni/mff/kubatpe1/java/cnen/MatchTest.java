/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.kubatpe1.java.cnen;

import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotatedText;
import cz.cuni.mff.kubatpe1.java.cnen.anotations.AnotationParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.TreexParser;
import cz.cuni.mff.kubatpe1.java.cnen.parsing.exceptions.TreeParsingException;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.SentenceTree;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.TreeTextMatcher;
import cz.cuni.mff.kubatpe1.java.cnen.sentencetree.exceptions.TextMatchingException;
import java.util.List;

/**
 *
 * @author petrkubat
 */
public class MatchTest {
    
    public static void main(String[] args) throws TreeParsingException, TextMatchingException, AnotationParsingException {
        TreexParser parser = new TreexParser();
        List<SentenceTree> trees = parser.parseTree("/Users/petrkubat/Downloads/result.treex");
        
        TreeTextMatcher matcher = new TreeTextMatcher(trees);
        
        String text = "Český jazyk neboli <ne type=unknown>čeština</ne> je západoslovanský jazyk, nejvíce příbuzný se <ne>slovenštinou</ne>, poté polštinou a lužickou srbštinou. Patří mezi slovanské jazyky, do rodiny jazyků indoevropských. Čeština se vyvinula ze západních nářečí praslovanštiny na konci 10. století. Česky psaná literatura se objevuje od 14. století. První písemné památky jsou však již z 12. století. Dělí se na spisovnou č., určenou pro oficiální styk (je kodifikována v mluvnicích a slovnících), a nespisovnou č., která zahrnuje dialekty (nářečí) a sociolekty (slangy) včetně vulgarismů a argotu. Spisovná čeština má dvě podoby: vypjatě spisovnou a hovorovou. Hovorovou češtinu je třeba odlišovat od češtiny obecné.";
        String text2 = "Vede ji žena , jmenuje se Ann Suba .\n" +
"Zaplatil jsem mu dopředu , a tak jsem zvědavý , jestli se vrátí .\n" +
"Polovičatost modu vivendi mezi Izraelem a Bohem , placená zamlžováním boží pravdy a lidského hříchu , znevážením Boha i člověka , jitřila Ježíše tak , že se rozhodl nemlčet a handlování s Bohem skončit , ve skutečnosti však experiment s jeho všemohoucností vyostřit .\n" +
"Ohmatávali kůru stromu , vdechovali větřík a hledali neznámo co .\n" +
"Myslím , že i legendární ošetřovatelka Florence Nigthingale , která je v Británii dávána za příklad člověka s posláním , by asi dala podobnou odpověď o svém zaměstnání .\n" +
"MS - 202 - 08 / 97 Šť . bígl s PP po výborných rodičích .";
        
        
        AnotatedText anotated = AnotatedText.parseText(text, "ne");
        
        matcher.matchWithText(anotated);
        
        System.out.println();
    }
}
