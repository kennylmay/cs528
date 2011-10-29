import java.util.HashMap;
import java.util.LinkedList;


public class PlayerStats extends HashMap<String, Object> {
	//TODO this will return a player stats object formed by combining a list of items
	public static PlayerStats calculateStats(LinkedList<LoLItem> items) {
		PlayerStats ps = new PlayerStats();
		
		int health = 0;
		int mana  = 0;
		int healthRegen = 0;
		int armor = 0;
		int magicResist = 0;
		int damage = 0;
		int abilityPower = 0;
		int critical = 0;
		int attackSpeed = 0;
		int lifeSteal = 0;
		int spellVamp = 0;
		int dodge = 0;
		int cdr = 0;
		int tenacity = 0;
		int movement = 0;
		
		for(LoLItem item: items) {
			//System.out.println(item);
			health += getInt(item, "Health");
			mana += getInt(item, "Mana");
			healthRegen += getInt(item, "HealthRegen");
			armor += getInt(item, "Armor");
			magicResist += getInt(item, "Resist");
			damage += getInt(item, "Damage");
			abilityPower += getInt(item, "AbilityPower");
			critical += getInt(item, "Critical");
			attackSpeed += getInt(item, "AttackSpeed");
			lifeSteal +=  getInt(item, "Lifesteal");
			spellVamp += getInt(item, "SpellVamp");
			dodge += getInt(item, "Dodge");
			cdr += getInt(item, "CDR");;
			tenacity += getInt(item, "Tenacity");
			movement +=  getInt(item, "Movement");
		}
		
		ps.put("Health", health);
		ps.put("Mana", mana);
		ps.put("HealthRegen", healthRegen );
		ps.put("Armor", armor);
		ps.put("Resist", magicResist);
		ps.put("Damage", damage);
		ps.put("AbilityPower", abilityPower);
		ps.put("Critical", critical);
		ps.put("AttackSpeed", attackSpeed);
		ps.put("LifeSteal", lifeSteal);
		ps.put("SpellVamp", spellVamp);
		ps.put("Dodge", dodge);
		ps.put("CDR", cdr);
		ps.put("Tenacity", tenacity);
		ps.put("Movement", movement);
		System.out.println(ps);
		return ps;
	}
	
	private static int getInt(LoLItem item, String attrName) {
		if(item.containsKey(attrName)) {
			return  Integer.parseInt((String)item.get(attrName));
		}
		return 0;
	}
	
	public static HashMap<String, Double> heuristicUnits = new HashMap<String, Double>();
	static {
		heuristicUnits.put("Health",475/180.0);     // ruby crystal
		heuristicUnits.put("Armor", 300/18.0);       // cloth armor
		heuristicUnits.put("Resist", 740/48.0); // negatron cloak
		heuristicUnits.put("Damage", 975/30.0);      // pick axe
		heuristicUnits.put("AbilityPower",435/20.0);// ability tome
		heuristicUnits.put("Critical", 400/8.0);     // brawler's
		heuristicUnits.put("AttackSpeed", 420/15.0); // dagger
		heuristicUnits.put("Lifesteal", 450/12.0);   // vampiric scepter
		heuristicUnits.put("SpellVamp", 700/15.0);   // GUESTIMATE, there's no item that gives flat spellvamp
		heuristicUnits.put("CDR", 1050/15.0);        // GUESTIMATE, there's no item that gives flat cdr 
		
		
	}
	

	
}