import java.util.HashMap;
import java.util.LinkedList;

public class GUIProcessor {

	static LinkedList<Token> buildTokenList(CaseData caseData,
			HashMap<String, LoLItem> itemData) {
		LinkedList<Token> allToks = new LinkedList<Token>();
		// Get the core attributes for each player's role
		if (caseData.get("playerChampionRole") != null) {
			LinkedList<Token> playerCore = getAttributes(
					(String[]) caseData.get("playerChampionRole"), true);
			allToks.addAll(playerCore);
		}
		if (caseData.get("opponentChampionRole") != null) {
			LinkedList<Token> opponentCore = getAttributes(
					(String[]) caseData.get("opponentChampionRole"), false);
			allToks.addAll(opponentCore);
		}

		return allToks;

	}

	// This function returns the core attributes for a type of champ; for
	// example, a Fighter would have attack damage, armor, armor pen, health,
	// etc
	public static LinkedList<Token> getAttributes(String[] c, boolean isPlayer) {
		LinkedList<Token> attr = new LinkedList<Token>();
		if (isPlayer)
			attr.add(new Token(Token.Typ.Player, Token.PlayerTyp.Us));
		else
			attr.add(new Token(Token.Typ.Player, Token.PlayerTyp.Them));

		for (String champ : c) {
			if (champ.equals("assasin")) {
				attr.add(new Token(Token.Typ.Attribute, "Critical"));
			} else if (champ.equals("tank")) {
				attr.add(new Token(Token.Typ.Attribute, "Health"));
			} else if (champ.equals("mage")) {
				attr.add(new Token(Token.Typ.Attribute, "AbilityPower"));
			} else if (champ.equals("fighter")) {
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
			} else if (champ.equals("support")) {
				attr.add(new Token(Token.Typ.Attribute, "CDR"));
			} else if (champ.equals("melee")) {
				attr.add(new Token(Token.Typ.Attribute, "LifeSteal"));
			} else if (champ.equals("stealth")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
			} else if (champ.equals("carry")) {
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
			} else if (champ.equals("pusher")) {
				attr.add(new Token(Token.Typ.Attribute, "Armor"));
			} else if (champ.equals("ranged")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
			}
		}
		return attr;
	}

}
