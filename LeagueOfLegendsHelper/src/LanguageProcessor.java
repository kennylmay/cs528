import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Vector;

public class LanguageProcessor {
        HashMap<String, Vector<String>> synonyms = new HashMap<String, Vector<String>>();
        Vector<String> uselessWords = new Vector<String>();
        
        /**
         * Only setup the word lists in the constructor.
         */
        public LanguageProcessor(){
                intializeThesaurus();
                intializeUselessWords();
        }
        
        /**
         * Ask the user for an English sentence that we will then try to interpret
         * into its very basic meaning.
         * 
         * @return The user's "Meaning" of the sentence they typed
         */
        public String askQuestion(){
                String userInput = JOptionPane.showInputDialog("What can I help you with?");
                if (userInput == null){
                        return "";
                }
                
                String meaning = userInput.toLowerCase();
            meaning = filterUselessWords(meaning);
                meaning = determineMeaning(meaning);
                
                return meaning;
        }
        
        /**
         * This function will remove any words that are included in
         * the uselessWords Vector. This function will also remove
         * any unneeded punctuation.
         * 
         * @return The sentence without meaningless words.
         */
        private String filterUselessWords(String sentence){
                sentence = sentence.replaceAll("'","");
                sentence = sentence.replaceAll(".",""); 
                sentence = sentence.replaceAll(",",""); 
                
                for(String word : uselessWords){
                  sentence = sentence.replaceAll(word + " ", "");       
                  sentence = sentence.replaceAll(" " + word, "");       
                }
                return sentence;
        }
        
        /**
         * This function will replace the user's words with words
         * that our program can understand. It will find any words
         * defined in the "synonyms" HashMap and replace them with 
         * their key word.
         * 
         * @return The sentence with only key words.
         */
        private String determineMeaning(String sentence){
                for(String word: synonyms.keySet()){
                        for (String syn : synonyms.get(word)){
                                sentence = sentence.replaceAll(syn, word);
                        }
                }
                return sentence;
        }
        
        /**
         * Setup the synonym list. The baseWord is the word you would
         * like the word or phrase to be replaced with.
         */
        private void intializeThesaurus(){
                String baseWord;
                Vector<String> syns = new Vector<String>();
                
                baseWord = "Damage";    
                syns.add("attack damage");
                syns.add("physical damage");
                syns.add("need more damage");
                syns.add("ad");
                syns.add("kill people");
                syns.add("kill");
                this.synonyms.put(baseWord, syns);
                
                syns = new Vector<String>();
                baseWord = "Tanky";
                syns.add("unkillable");
                syns.add("hard to kill");
                syns.add("keep dieing");
                syns.add("easily killed");
                this.synonyms.put(baseWord, syns);
                
                syns = new Vector<String>();
                baseWord = "Mana";
                syns.add("mana");
                syns.add("spam ablities");
                syns.add("cant use ablities");
                syns.add("need more mana");
                this.synonyms.put(baseWord, syns);
        }
        
        /**
         * Setup a list of words that do not have any real 
         * meaning in the sentence.
         */
        private void intializeUselessWords(){
                uselessWords.add("want");
                uselessWords.add("i");
                uselessWords.add("am");
                uselessWords.add("being");
                uselessWords.add("real");
                uselessWords.add("my");
                uselessWords.add("enough");
                uselessWords.add("to");
        }
        
}