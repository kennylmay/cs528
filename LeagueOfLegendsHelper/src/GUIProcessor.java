import java.util.HashMap;
import java.util.LinkedList;

public class GUIProcessor {

	static LinkedList<Token> buildTokenList(CaseData caseData,
			HashMap<String, LoLItem> itemData) {

		// Get the core attributes for each player's role
		if (caseData.get("playerChampionRole") != null) {
			LinkedList<Token> playerCore = getAttributes(
					(String[]) caseData.get("playerChampionRole"), true);
			if (caseData.get("opponentChampionRole") != null) {
				playerCore.add(new Token(Token.Typ.LogicalOp,
						Token.LogicalOpTyp.AND));

				LinkedList<Token> opponentCore = getAttributes(
						(String[]) caseData.get("opponentChampionRole"), false);
				playerCore.addAll(opponentCore);
			}
			return playerCore;
		}

		return null;

	}

	// This function returns the core attributes for a type of champ; for
	// example, a Fighter would have attack damage, armor, armor pen, health,
	// etc
	public static LinkedList<Token> getAttributes(String[] c, boolean isPlayer) {
		LinkedList<Token> attr = new LinkedList<Token>();
		if (isPlayer)
			attr.add(new Token(Token.Typ.Player, Token.PlayerTyp.Them));
		else
			attr.add(new Token(Token.Typ.Player, Token.PlayerTyp.Us));

		for (String champ : c)
			if (champ == "assasin") {
				attr.add(new Token(Token.Typ.Attribute, "ArmorPenetration"));
				attr.add(new Token(Token.Typ.Attribute, "Critical"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
			} else if (champ.equals("tank")) {
				attr.add(new Token(Token.Typ.Attribute, "Armor"));
				attr.add(new Token(Token.Typ.Attribute, "Health"));
				attr.add(new Token(Token.Typ.Attribute, "MagicResist"));
			} else if (champ.equals("mage")) {
				attr.add(new Token(Token.Typ.Attribute, "AbilityPower"));
				attr.add(new Token(Token.Typ.Attribute, "MagicPenetration"));
				attr.add(new Token(Token.Typ.Attribute, "CDR"));
				attr.add(new Token(Token.Typ.Attribute, "SpellVamp"));
			} else if (champ.equals("fighter")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
				attr.add(new Token(Token.Typ.Attribute, "Critical"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
				attr.add(new Token(Token.Typ.Attribute, "LifeSteal"));
			} else if (champ.equals("support")) {
				attr.add(new Token(Token.Typ.Attribute, "AbilityPower"));
				attr.add(new Token(Token.Typ.Attribute, "CDR"));
				attr.add(new Token(Token.Typ.Attribute, "SpellVamp"));
			} else if (champ.equals("melee")) {
				attr.add(new Token(Token.Typ.Attribute, "Armor"));
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
				attr.add(new Token(Token.Typ.Attribute, "LifeSteal"));
			} else if (champ.equals("stealth")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
			} else if (champ.equals("carry")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
				attr.add(new Token(Token.Typ.Attribute, "Critical"));
				attr.add(new Token(Token.Typ.Attribute, "LifeSteal"));
			} else if (champ.equals("pusher")) {
				attr.add(new Token(Token.Typ.Attribute, "Armor"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
				attr.add(new Token(Token.Typ.Attribute, "Health"));
			} else if (champ.equals("ranged")) {
				attr.add(new Token(Token.Typ.Attribute, "AttackSpeed"));
				attr.add(new Token(Token.Typ.Attribute, "Damage"));
				attr.add(new Token(Token.Typ.Attribute, "LifeSteal"));
			}
		return attr;
	}

}
