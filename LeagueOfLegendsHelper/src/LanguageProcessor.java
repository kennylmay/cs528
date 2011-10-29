import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JOptionPane;

public class LanguageProcessor {
	HashMap<String, Vector<String>> synonyms = new HashMap<String, Vector<String>>();
	Vector<String> uselessWords = new Vector<String>();
	HashMap<String, LoLItem> items;

	public enum ChampionRole {
		Assasin, Tank, Mage, Fighter, Support
	};

	/**
	 * Only setup the word lists in the constructor.
	 */
	public LanguageProcessor(HashMap<String, LoLItem> Items) {
		items = Items;
	}

	/**
	 * Ask the user for an English sentence that we will then try to interpret
	 * into its very basic meaning.
	 * 
	 * @return The user's "Meaning" of the sentence they typed
	 */
	public CaseData askQuestion() {
		CaseData caseData = new CaseData();

		// Determine what kind of Champion the player is.
		String userInput = JOptionPane
				.showInputDialog("Hi I’m here to help you choose your next LoL item. \n"
						+ "I’m going to ask a few questions.  \n"
						+ "What is the role of your champion? \n"
						+ "Choose one of: [Assassin, Tank, Mage, Fighter, Support]");

		while ((!userInput.equalsIgnoreCase("assassin"))
				&& (!userInput.equalsIgnoreCase("tank"))
				&& (!userInput.equalsIgnoreCase("mage"))
				&& (!userInput.equalsIgnoreCase("fighter"))
				&& (!userInput.equalsIgnoreCase("support"))) {
			userInput = JOptionPane
					.showInputDialog("Choose one of: [Assassin, Tank, Mage, Fighter, Support]");
		}

		if (userInput.equalsIgnoreCase("assassin")) {
			caseData.put("playerChampionRole", ChampionRole.Assasin);
		} else if (userInput.equalsIgnoreCase("tank")) {
			caseData.put("playerChampionRole", ChampionRole.Tank);
		} else if (userInput.equalsIgnoreCase("mage")) {
			caseData.put("playerChampionRole", ChampionRole.Mage);
		} else if (userInput.equalsIgnoreCase("figher")) {
			caseData.put("playerChampionRole", ChampionRole.Fighter);
		} else if (userInput.equalsIgnoreCase("support")) {
			caseData.put("playerChampionRole", ChampionRole.Support);
		}

		// Determine the kind of Champion the opponent is.
		userInput = "";
		while ((!userInput.equalsIgnoreCase("assassin"))
				&& (!userInput.equalsIgnoreCase("tank"))
				&& (!userInput.equalsIgnoreCase("mage"))
				&& (!userInput.equalsIgnoreCase("fighter"))
				&& (!userInput.equalsIgnoreCase("support"))) {
			userInput = JOptionPane
					.showInputDialog("What is the role of your opponent?\n"
							+ "Choose one of: [Assassin, Tank, Mage, Fighter, Support]");
		}

		if (userInput.equalsIgnoreCase("assassin")) {
			caseData.put("opponentChampionRole", ChampionRole.Assasin);
		} else if (userInput.equalsIgnoreCase("tank")) {
			caseData.put("opponentChampionRole", ChampionRole.Tank);
		} else if (userInput.equalsIgnoreCase("mage")) {
			caseData.put("opponentChampionRole", ChampionRole.Mage);
		} else if (userInput.equalsIgnoreCase("fighter")) {
			caseData.put("opponentChampionRole", ChampionRole.Fighter);
		} else if (userInput.equalsIgnoreCase("support")) {
			caseData.put("opponentChampionRole", ChampionRole.Support);
		}

		// Determine how much money the player has
		int money = -1;
		while (money < 0) {

			userInput = JOptionPane
					.showInputDialog("How much gold can you spend?\n"
							+ " If you don’t care about the amount of gold, say 0.");
			try {
				money = Integer.parseInt(userInput);
				caseData.put("Gold", money);
			} catch (NumberFormatException nfe) {
			}
		}

		// Determine the players items.
		userInput = JOptionPane
				.showInputDialog("Do you have any items already? If so, list them.");
		if (userInput.isEmpty()) {
			caseData.put("playerItems", null);
		} else {
			boolean correctList = false;
			LinkedList<String> itemList = splitList(userInput);
			while (!correctList && !userInput.isEmpty()) {
				for (String item : itemList) {
					if (!items.containsKey(item)) {
						correctList = false;
						System.out.println(item);
						userInput = JOptionPane.showInputDialog(" I don’t recognize the item "+ item + 
																"\n Do you have any items? If so, list them comma seperated.");
						break;
					}else{
						correctList = true;
					}
				}
			}

			caseData.put("playerItems", splitList(userInput));
		}

		// Determine if the opponent has any items.
		userInput = JOptionPane
				.showInputDialog("Does your opponent have any items? If so, list them.");
		if (userInput.isEmpty()) {
			caseData.put("opponentItems", null);
		} else {
			boolean correctList = false;
			LinkedList<String> itemList = splitList(userInput);
			while (!correctList && !userInput.isEmpty()) {
				for (String item : itemList) {
					if (!items.containsKey(item)) {
						correctList = false;
						userInput = JOptionPane.showInputDialog(" I don’t recognize the item "+ item + 
																"\n Does your opponent have any items? If so, list themcomma seperated.");
						break;
					}else{
						correctList = true;
					}
				}
			}
			caseData.put("opponentItems", splitList(userInput));
		}

		return caseData;
	}

	/**
	 * This function will remove any words that are included in the uselessWords
	 * Vector. This function will also remove any unneeded punctuation.
	 * 
	 * @return The sentence without meaningless words.
	 */
	private LinkedList<String> splitList(String list) {
		LinkedList<String> items = new LinkedList<String>();
		String splitList[] = list.split(",");
		for (String item : splitList) {
			item = item.trim();
			items.add(item);
		}
		return items;
	}

}
