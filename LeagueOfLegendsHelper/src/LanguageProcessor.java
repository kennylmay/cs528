import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

public class LanguageProcessor {

	private static Hashtable<String, Token> synToks = initializeSynToks();

	/**
	 * Ask the user for an English sentence that we will then try to interpret
	 * into a list of tokens that can be matched to the grammar.
	 * 
	 * @param sentence
	 *            The natural language sentence to be processed.
	 * @return A linked list of tokens that represent the sentence.
	 */
	public LinkedList<Token> askQuestion(String sentence) {

		if (sentence.length() == 0) {
			return null;
		}

		return processSentence(formatSentence(sentence));
	}

	/**
	 * @param sentence
	 *            The pre-processed sentence to be processed.
	 * @return A token list representing the sentence.
	 */
	private LinkedList<Token> processSentence(String sentence) {
		LinkedList<Token> sentTokens = new LinkedList<Token>();

		String[] words = sentence.split(" ");
		Vector<String> wordsVector;
		String tmpString;

		// Work from left to right breaking the sentence into phrases that
		// relate to tokens.
		for (int left = 0; left < words.length;) {

			// Build the phrase to check.
			wordsVector = new Vector<String>();

			// Put all words that we have not already matched into the
			// wordsVector.
			for (int i = 0; i < words.length - left; i++) {
				wordsVector.add(words[left + i]);
			}

			// Match the max possible words on the left side of the wordVector
			// to a phrase.
			while (!wordsVector.isEmpty()) {

				// Build the working phrase that will be checked against the
				// synonym list.
				tmpString = "";
				for (int j = 0; j < wordsVector.size(); j++) {
					tmpString += wordsVector.elementAt(j) + " ";
				}
				// Clean up the trailing space.
				tmpString = tmpString.trim();

				// If the working phrase is in the synonym list then get the
				// related token and added it to the token sentence to be
				// returned.
				if (synToks.containsKey(tmpString)) {
					// FOUND IT!!!

					sentTokens.add(synToks.get(tmpString));

					// Move left past the phrase we found.
					left += wordsVector.size();
					// Exit the while loop.
					break;
				}

				// Remove the left most word from the wordsVector since it was
				// not found to be part of a phrase. This will allow us to check
				// the next smaller phrase in the sentence.
				wordsVector.remove(wordsVector.size() - 1);

			}

			// If the current left most word was not found to be in a known
			// phrase then mark it as a unknown 'NoOp' token.
			if (wordsVector.isEmpty()) {
				sentTokens.add(new Token(Token.Typ.NoOp, words[left]));
				left++;
			}
		}

		// Return the token sentence.
		return sentTokens;

	}

	/**
	 * This function will remove any unneeded punctuation and convert everything
	 * to lowercase.
	 * 
	 * @param sentence
	 *            The natural language sentence to be pre-processed.
	 * @return Returns a sentence without special characters.
	 */
	private String formatSentence(String sentence) {
		sentence = sentence.toLowerCase();
		sentence = sentence.replaceAll("'", "");
		sentence = sentence.replaceAll("[.]", "");
		sentence = sentence.replaceAll(",", "");

		return sentence;
	}

	/**
	 * @return Returns a fully initialized list of synonym token mappings.
	 */
	private static Hashtable<String, Token> initializeSynToks() {

		Token tmpToken = null;

		Hashtable<String, Token> synonyms = new Hashtable<String, Token>();

		// Damage Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Damage");

		synonyms.put("damage", tmpToken);
		synonyms.put("attack damage", tmpToken);
		synonyms.put("physical damage", tmpToken);
		synonyms.put("need more damage", tmpToken);
		synonyms.put("ad", tmpToken);
		synonyms.put("kill people", tmpToken);
		synonyms.put("kill", tmpToken);

		// Tanky Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Tanky");

		synonyms.put("tanky", tmpToken);
		synonyms.put("unkillable", tmpToken);
		synonyms.put("hard to kill", tmpToken);
		synonyms.put("dont want to die", tmpToken);

		// Mana Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Mana");

		synonyms.put("mana", tmpToken);
		synonyms.put("spam ablities", tmpToken);
		synonyms.put("cant use abilities", tmpToken);
		synonyms.put("need more mana", tmpToken);
		synonyms.put("want to harass", tmpToken);

		// Magic Resist Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Resist");

		synonyms.put("magic resist", tmpToken);
		synonyms.put("magicresist", tmpToken);
		synonyms.put("resist", tmpToken);
		synonyms.put("mr", tmpToken);
		synonyms.put("counter for ability power", tmpToken);
		synonyms.put("counter for ap", tmpToken);
		synonyms.put("counter for mage", tmpToken);
		synonyms.put("counter for magic", tmpToken);

		// Ability Power Phrases
		tmpToken = new Token(Token.Typ.Attribute, "AbilityPower");

		synonyms.put("magic", tmpToken);
		synonyms.put("magic damage", tmpToken);
		synonyms.put("magic power", tmpToken);
		synonyms.put("ability power", tmpToken);
		synonyms.put("ap", tmpToken);
		synonyms.put("magic burst", tmpToken);
		synonyms.put("burst magic", tmpToken);
		synonyms.put("counter for damage", tmpToken);
		synonyms.put("counter for armor", tmpToken);

		// Armor Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Armor");

		synonyms.put("armor", tmpToken);
		synonyms.put("killing", tmpToken);
		synonyms.put("die less", tmpToken);
		synonyms.put("die too much", tmpToken);
		synonyms.put("getting killed", tmpToken);
		synonyms.put("has attack damage", tmpToken);
		synonyms.put("has lots of attack damage", tmpToken);
		synonyms.put("has a lot of attack damage", tmpToken);
		synonyms.put("keep dieing", tmpToken);
		synonyms.put("counter for attack damage", tmpToken);

		// Speed Phrases
		tmpToken = new Token(Token.Typ.Attribute, "AttackSpeed");

		synonyms.put("speed", tmpToken);
		synonyms.put("too slow", tmpToken);
		synonyms.put("faster", tmpToken);
		synonyms.put("fast", tmpToken);
		synonyms.put("dps", tmpToken);
		synonyms.put("counter for health", tmpToken);
		synonyms.put("attack speed", tmpToken);

		// Tenacity Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Tenacity");

		synonyms.put("tenacity", tmpToken);
		synonyms.put("getting stuck", tmpToken);
		synonyms.put("getting stunned", tmpToken);
		synonyms.put("easily stunned", tmpToken);
		synonyms.put("stunned", tmpToken);
		synonyms.put("feared", tmpToken);
		synonyms.put("running around", tmpToken);
		synonyms.put("uncontrollable", tmpToken);
		synonyms.put("embolized", tmpToken);
		synonyms.put("dembolized", tmpToken);
		synonyms.put("antistun", tmpToken);
		synonyms.put("antifear", tmpToken);
		synonyms.put("counter for stun", tmpToken);
		synonyms.put("counter for fear", tmpToken);

		// Lifesteal Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Lifesteal");

		synonyms.put("lifesteal", tmpToken);
		synonyms.put("duel", tmpToken);
		synonyms.put("win a duel", tmpToken);
		synonyms.put("one on one", tmpToken);
		synonyms.put("1v1", tmpToken);
		synonyms.put("1 v 1", tmpToken);
		synonyms.put("1vs1", tmpToken);
		synonyms.put("1 vs 1", tmpToken);
		synonyms.put("counter for health", tmpToken);

		// Crit Strike Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Critical");

		synonyms.put("burst", tmpToken);
		synonyms.put("all at once", tmpToken);
		synonyms.put("at once", tmpToken);
		synonyms.put("kill quickly", tmpToken);
		synonyms.put("kill fast", tmpToken);
		synonyms.put("kill faster", tmpToken);
		synonyms.put("kill them first", tmpToken);
		synonyms.put("kill them fast", tmpToken);
		synonyms.put("kill them faster", tmpToken);
		synonyms.put("critical", tmpToken);
		synonyms.put("critical strike", tmpToken);
		synonyms.put("crit", tmpToken);
		synonyms.put("counter for armor", tmpToken);

		// Health Regen Phrases
		tmpToken = new Token(Token.Typ.Attribute, "HealthRegen");

		synonyms.put("health regen", tmpToken);
		synonyms.put("gain health", tmpToken);
		synonyms.put("regain health", tmpToken);
		synonyms.put("refill health", tmpToken);
		synonyms.put("build health", tmpToken);
		synonyms.put("rebuild health", tmpToken);
		synonyms.put("recharge health", tmpToken);
		
		// Health Regen Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Health");
		
		synonyms.put("health", tmpToken);
		synonyms.put("more health", tmpToken);
		synonyms.put("more life", tmpToken);

		
		// Health Regen Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Dodge");
		
		synonyms.put("dodge", tmpToken);
		synonyms.put("make miss", tmpToken);
		synonyms.put("missing", tmpToken);
		synonyms.put("miss", tmpToken);
		
		
		// Sustain Phrases
		tmpToken = new Token(Token.Typ.Attribute, "Sustain");
		
		synonyms.put("sustain", tmpToken);
		synonyms.put("sustainment", tmpToken);
		synonyms.put("stay in lane", tmpToken);
		

		// Cool Down Reduction Phrases
		tmpToken = new Token(Token.Typ.Attribute, "CDR");

		synonyms.put("cooldown reduction", tmpToken);
		synonyms.put("cdr", tmpToken);
		synonyms.put("use abilities faster", tmpToken);
		synonyms.put("use more abilities", tmpToken);
		synonyms.put("wait time", tmpToken);
		
		// Offense
		tmpToken = new Token(Token.Typ.Attribute, "Offensive");

		synonyms.put("offense", tmpToken);
		synonyms.put("offensive", tmpToken);
		synonyms.put("offence", tmpToken);
		synonyms.put("attack", tmpToken);


		// Defense
		tmpToken = new Token(Token.Typ.Attribute, "Defensive");

		synonyms.put("defense", tmpToken);
		synonyms.put("defend", tmpToken);
		synonyms.put("defensive", tmpToken);
		synonyms.put("protect", tmpToken);
		
		
		// Movement Speed
		tmpToken = new Token(Token.Typ.Attribute, "Movement");

		synonyms.put("run faster", tmpToken);
		synonyms.put("catch", tmpToken);
		synonyms.put("catching", tmpToken);
		synonyms.put("getting caught", tmpToken);
		synonyms.put("caught easily", tmpToken);
		synonyms.put("cant get away", tmpToken);
		
		
		// Player 'US' Phrases
		tmpToken = new Token(Token.Typ.Player, Token.PlayerTyp.Us);

		synonyms.put("i", tmpToken);

		// Player 'Opp' Phrases
		tmpToken = new Token(Token.Typ.Player, Token.PlayerTyp.Them);

		synonyms.put("they", tmpToken);
		synonyms.put("he", tmpToken);
		synonyms.put("she", tmpToken);
		synonyms.put("it", tmpToken);
		synonyms.put("my enemy", tmpToken);

		// Logical Ops And
		tmpToken = new Token(Token.Typ.LogicalOp, Token.LogicalOpTyp.AND);

		synonyms.put("and", tmpToken);

		// Logical Ops Not
		tmpToken = new Token(Token.Typ.LogicalOp, Token.LogicalOpTyp.NOT);

		synonyms.put("not", tmpToken);
		synonyms.put("but not", tmpToken);

		return synonyms;
	}

}