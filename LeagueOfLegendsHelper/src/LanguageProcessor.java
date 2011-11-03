import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class LanguageProcessor {
	HashMap<String, Vector<String>> synonyms = new HashMap<String, Vector<String>>();
	Vector<String> opponentWords = new Vector<String>();
	Vector<String> playerWords = new Vector<String>();

	Hashtable<String, Token> synToks = null;
	/**
	 * Only setup the word lists in the constructor.
	 */
	public LanguageProcessor() {
		//intializeThesaurus();
		intializeSynToks();
		//intializePlayerSubjects();
		//intializeOpponentSubjects();
	}

	/**
	 * Ask the user for an English sentence that we will then try to interpret
	 * into its very basic meaning.
	 * 
	 * @return The user's "Meaning" of the sentence they typed
	 */
	public String askQuestion(String sentence) {

		if (sentence.length() == 0) {
			return "";
		}

		String formattedsentence = formatSentence(sentence);
		//String subject = findSubject(formattedsentence);
		//String attribute = findAttribute(formattedsentence);

		
		
		JOptionPane.showMessageDialog(null, formattedsentence);
		JOptionPane.showMessageDialog(null, processSentence(formattedsentence).toString());
		return "Fucking Happy";
	}


	private Vector<Token> processSentence(String sentence) {
		Vector<Token> sentTokens = new Vector<Token>();

		String[] words = sentence.split(" ");
		Vector<String> wordsVector;
		String tmpString;

		for (int left = 0; left < words.length; ) {

			// Build the phrase to check.
			wordsVector = new Vector<String>();

			for (int i = 0; i < words.length - left; i++) {
				wordsVector.add(words[left + i]);
			}

			while (!wordsVector.isEmpty()) {

				tmpString = "";
				for (int j = 0; j < wordsVector.size(); j++) {
					tmpString += wordsVector.elementAt(j) + " ";
				}
				tmpString.trim();
				
				if (synToks.containsKey(tmpString)) {
					// FOUND IT!!!

					sentTokens.add(synToks.get(tmpString));

					// Move left past the phrase we found.
					left = wordsVector.size();
					break;
				}

				wordsVector.remove(wordsVector.size() - 1);

			}
			
			if(wordsVector.isEmpty()) {
				sentTokens.add(new Token(Token.Typ.NoOp, words[left]));
				left++;
			}
		}

		return sentTokens;

	}
	/**
	 * This function will remove any words that are included in the uselessWords
	 * Vector. This function will also remove any unneeded punctuation.
	 * 
	 * @return The sentence without meaningless words.
	 */
	private String formatSentence(String sentence) {
		sentence = sentence.toLowerCase();
		sentence = sentence.replaceAll("'", "");
		sentence = sentence.replaceAll("[.]", "");
		sentence = sentence.replaceAll(",", "");

		return sentence;
	}

	/**
	 * This function will replace the user's words with words that our program
	 * can understand. It will find any words defined in the "synonyms" HashMap
	 * and replace them with their key word.
	 * 
	 * @return The sentence with only key words.
	 */
	private String findAttribute(String sentence) {		
		for (String word : synonyms.keySet()) {
			for (String syn : synonyms.get(word)) {
				sentence = sentence.replaceAll(syn, word);
			}
		}
		
		for (String word : sentence.split(" ")) {
			if (!synonyms.containsKey(word)) {
				sentence = sentence.replaceAll(word, " NOOP ");
			}
		}

		return sentence;
	}

	/**
	 * Setup the synonym list. The baseWord is the word you would like the word
	 * or phrase to be replaced with.
	 */
	private void intializeThesaurus() {
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

		syns = new Vector<String>();
		baseWord = "MagicResist";
		syns.add("magic resist");
		syns.add("magicresist");
		this.synonyms.put(baseWord, syns);
	}
	
	/**
	 * 
	 */
	private void intializeSynToks() {
		
		Token tmpToken = null;
		
		synToks = new Hashtable<String,Token>();
		
		
		// Damage Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Damage");
		
		synToks.put("attack damage", tmpToken);
		synToks.put("physical damage", tmpToken);
		synToks.put("need more damage", tmpToken);
		synToks.put("ad", tmpToken);
		synToks.put("kill people", tmpToken);
		synToks.put("kill", tmpToken);

		
		// Tanky Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Tanky");

		synToks.put("unkillable", tmpToken);
		synToks.put("hard to kill", tmpToken);
		synToks.put("keep dieing", tmpToken);
		synToks.put("easily killed", tmpToken);
		
		
		// Mana Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Mana");
		
		synToks.put("mana", tmpToken);
		synToks.put("spam ablities", tmpToken);
		synToks.put("cant use abilities", tmpToken);
		synToks.put("need more mana", tmpToken);
		
		// Magic Resist Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Resist");
		
		synToks.put("magic resist", tmpToken);
		synToks.put("magicresist", tmpToken);
		synToks.put("resist", tmpToken);
		synToks.put("mr", tmpToken);
		
		// Player 'US' Phrases
		tmpToken = new Token(Token.Typ.Player, "US");
		
		synToks.put("i", tmpToken);
		
		// Player 'Opp' Phrases
		tmpToken = new Token(Token.Typ.Player, "US");
		
		synToks.put("they", tmpToken);	
		synToks.put("he", tmpToken);
		synToks.put("she", tmpToken);
		synToks.put("it", tmpToken);
		synToks.put("my enemy", tmpToken);

	}

	/**
	 * This function will attempt to determine the subject of a sentence.
	 * 
	 * @param sentence
	 * @return String ("US" or "THEM")
	 */
	private String findSubject(String sentence) {
		int indexOfPlayerWord = 999999999;
		int indexOfOpponentWord = 999999999;
		for (String word : playerWords) {
			if (sentence.contains((word))) {
				indexOfPlayerWord = sentence.indexOf(word);
			}
		}
		for (String word : opponentWords) {
			if (sentence.contains((word))) {
				indexOfOpponentWord = sentence.indexOf(word);
			}
		}
		// / Return the first subject found as the the real subject.
		// / If no subject is found assume that the player is the
		// / subject.
		if (indexOfPlayerWord < indexOfOpponentWord) {
			return "US";
		} else if (indexOfPlayerWord > indexOfOpponentWord) {
			return "THEM";
		} else {
			return "US";
		}

	}

	/**
	 * Setup a list of words that could refer to the player as the subject.
	 */
	private void intializePlayerSubjects() {
		playerWords.add("i");
	}

	/**
	 * Setup a list of words that could possibility refer to the opponent as the
	 * subject
	 */
	private void intializeOpponentSubjects() {
		opponentWords.add("they");
		opponentWords.add("he");
		opponentWords.add("she");
		opponentWords.add("it");
		opponentWords.add("my enemy");
	}

}