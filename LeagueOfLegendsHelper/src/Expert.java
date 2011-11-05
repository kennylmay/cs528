import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class Expert {
	@SuppressWarnings("unchecked")
	static Answer suggestNextItem(CaseData caseData, HashMap<String, LoLItem> itemData, HashMap<String, String[]> itemHierarchy) {
		
		//Translate the currently possessed items of both players into statistical data. This will be used for heuristics later.
		PlayerStats playerStats = PlayerStats.calculateStats( (LinkedList<LoLItem>) caseData.get("playerItems"));
		PlayerStats opponentStats = PlayerStats.calculateStats((LinkedList<LoLItem>) caseData.get("opponentItems"));
		
	    System.out.println("Player: " + playerStats);
		System.out.println("Opponent: " + opponentStats);
		
		//Get the core attributes for each player's role		
		HashSet<String> playerCore   = coreAttributesFor((String[]) caseData.get("playerChampionRole"));
		HashSet<String> opponentCore = coreAttributesFor((String[]) caseData.get("opponentChampionRole"));
		
		//Get the list of general counters for each of the opponents core attributes
		LinkedList<Pair<String, String>> counters = new LinkedList<Pair<String, String>>();
		for(String attr : opponentCore) {
			String c = counterFor(attr);
			if(c.equals("")) {
				continue;
			}
			counters.add(new Pair<String, String>(attr,c));
		}
		
		//If there are any general counters available, check to see if there is a disparity between
		//the attribute and the counter. If there is, add it to the disparate properties list.
		LinkedList<Pair< Pair<String, String>, Integer>>  disparateProperties = new LinkedList<Pair< Pair<String,String>, Integer>>();
		for(Pair<String, String> p : counters) {
			//Get the heuristic value of each 
			int opponentValForAttr = heuristicForAttribute(p.first, opponentStats.get(p.first));
			int playerValForCounter = heuristicForAttribute(p.second, playerStats.get(p.second));
			
			if(opponentValForAttr > playerValForCounter) {
				//Add the pairing and the difference in heuristic value to the list of disparities
				disparateProperties.add(new Pair< Pair<String,String>, Integer>(p, opponentValForAttr - playerValForCounter));
			}
		}
		
		for(Pair< Pair<String, String>, Integer> prop : disparateProperties) {
			System.out.println("Lacking in " + prop.first.first + " vs  " + prop.first.second + " by " + prop.second  + " units");	
		}
		
		
		//Apply exceptions here. Remove any disparity from the disparity list that we don't care about. 
		  //For instance, if the opponent is a support character, don't care about the ability power/cdr/magic pen VS magic resist disparity.
		  //Another example, if the opponent is a mage, don't care about the attack damage/attack speed/ crit chance VS armor disparity. 
		  //Another example, if the player is support, don't care about the ability power VS magic pen disparity.
		

		//If any disparateProperties still exist, try to find items that fit the player's core properties and
		//satisfy some of the disparateProperties.
		
		//Choose the biggest disparity
		ItemRule disparityRule = null;
		int max = Integer.MIN_VALUE;
		Pair< Pair<String, String>, Integer> biggest = null;
		for(Pair< Pair<String, String>, Integer> prop : disparateProperties) {
		  if(prop.second > max) {
			  prop.second = max;
			  biggest = prop;
		  }
		}
	
		LinkedList <LoLItem> startingSet = new LinkedList<LoLItem>();
		//If a disparity exists, select the set of items that compensate for this disparity
		if(biggest != null) {
		  List<String> names = LeagueOfLegendsHelper.getAttributeItems(biggest.first.second);
		  Collections.sort(names);
		  
		  
		  for(String s : names) {
			  startingSet.add(itemData.get(s));
		  }
		}
			
		ItemRules rules = new ItemRules();
		for(LoLItem item : startingSet) {
			if(rules.isFighter.eval(item)) {
				System.out.println("Possible item: " + item.get("Item"));	
			}
		}
			
		return null;
	}
 
	
	public static LinkedList<LoLItem> getAppropriateItems(CaseData caseData, HashMap<String, LoLItem> itemData){
		//Get the core attributes for each player's role		
		HashSet<String> playerCore   = coreAttributesFor((String[]) caseData.get("playerChampionRole"));
		HashSet<String> opponentCore = coreAttributesFor((String[]) caseData.get("opponentChampionRole"));
		//Get the list of general counters for each of the opponents core attributes
		LinkedList<Pair<String, String>> counters = new LinkedList<Pair<String, String>>();
		
		for(String attr : opponentCore) {
			String c = counterFor(attr);
			if(c.equals("")) {
				continue;
			}
			counters.add(new Pair<String, String>(attr,c));
		}
		
		ItemRules rules = new ItemRules();
		for(LoLItem item : itemData.values()) {
			if(rules.isFighter.eval(item)) {
				System.out.println("Possible item: " + item.get("Item"));	
			}
		}
		
		return null;
		
	}


	public class Answer {
	   String response;
	   LinkedList<String> items;
	}
	
	//Types of champion roles
	public enum ChampionRole { Assasin, Tank, Mage, Fighter, Support };
	
	
	//This function returns the core attributes for a type of champ; for example, a Fighter would have attack damage, armor, armor pen, health, etc
	public static HashSet<String> coreAttributesFor(String[] c) {
		HashSet<String> attr = new HashSet<String>();
		for (String champ : c)
		if(champ == "assasin"){
			attr.add("ArmorPenetration");
			attr.add("Critical");
			attr.add("Damage");						
		}else if(champ == "tank"){
			attr.add("Armor");
			attr.add("Health");
			attr.add("MagicResist");
		}else if(champ == "mage"){
			attr.add("AbilityPower");
			attr.add("MagicPenetration");
			attr.add("CDR");
			attr.add("SpellVamp");
		}else if(champ == "fighter"){
			attr.add("AttackSpeed");
			attr.add("Critical");
			attr.add("Damage");
			attr.add("LifeSteal");	
		}else if(champ == "support"){
			attr.add("AbilityPower");
			attr.add("CDR");
			attr.add("SpellVamp");			
		}else if(champ == "melee"){
			attr.add("Armor");
			attr.add("AttackSpeed");
			attr.add("Damage");
			attr.add("LifeSteal");
		}else if(champ == "stealth"){
			attr.add("AttackSpeed");
			attr.add("Damage");
		}else if(champ == "carry"){
			attr.add("AttackSpeed");
			attr.add("Damage");
			attr.add("Critical");
			attr.add("LifeSteal");
		}else if(champ == "pusher"){
			attr.add("Armor");
			attr.add("Damage");
			attr.add("Health");
		}else if(champ == "ranged"){
			attr.add("AttackSpeed");
			attr.add("Damage");
			attr.add("LifeSteal");
		}
		return attr;
	}
	
	
    private static String counterFor(String attr) {

    	if(attr.equals("AbilityPower")) {
    		return "Resist";    		
    	}

    	else if(attr.equals("Damage")) {
    		return "Armor";
    	}
    	else if(attr.equals("Critical")) {
    		return "Armor";
    	}
    	else if(attr.equals("AttackSpeed")) {
    		return "Armor";
    	}
    	else if(attr.equals("Lifesteal")) {
    		return "Armor";
    	}
    	else if(attr.equals("SpellVamp")) {
    		return "Resist";
    	}
    	else if(attr.equals("CDR")) {
    		return "Resist";
    	}

    	//There is no general counter for this attribute.
    	return "";    	
    	
    }
    
    public static LinkedList<String> countersFor(String attr) {

    	LinkedList<String> attrs = new LinkedList<String>();
    	if(attr.equals("AbilityPower")) {
    		attrs.add("Resist");
    		attrs.add("Health");
   
    	}
    	else if(attr.equals("Damage")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Critical")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("AttackSpeed")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Lifesteal")) {
    	    attrs.add("Attack");
    		attrs.add("Armor");
    		attrs.add("AbilityPower");
    		attrs.add("AttackSpeed");
    	}
    	else if(attr.equals("SpellVamp")) {
    	    attrs.add("Attack");
    		attrs.add("Resist");
    		attrs.add("AbilityPower");
    		attrs.add("AttackSpeed");
    	}
    	else if(attr.equals("CDR")) {
    		attrs.add("Armor");
    		attrs.add("Resist");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Armor")) {
    		attrs.add("ArmorPen");
    		attrs.add("AbilityPower");
    		attrs.add("Attack");
    	}
    	else if(attr.equals("Resist")) {
    		attrs.add("MagicPen");
    		attrs.add("AbilityPower");
    		attrs.add("Attack");
    	}
    	else if(attr.equals("Tanky")) {
    		attrs.add("MagicPen");
    		attrs.add("ArmorPen");
    		attrs.add("AbilityPower");
    		attrs.add("Damage");
    		attrs.add("AttackSpeed");
    		attrs.add("Critical");
    	}
    	else if(attr.equals("Mage")) {
    		attrs.add("Resist");
    		attrs.add("Movement");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Fighter")) {
    		attrs.add("Armor");
    		attrs.add("Resist");
    		attrs.add("Health");
    		attrs.add("ArmorPen");
    		attrs.add("MagicPen");
    	}
    	else if(attr.equals("Support")) {
    		attrs.add("Attack");
    		attrs.add("Critical");
    		attrs.add("Damage");
    		attrs.add("AblityPower");
    		attrs.add("Lifesteal");
    	}
    	    	
    	return attrs;
    }
    
    
    
    //This function will assign a value to an investment in an attribute. It should return a number greater than or equal to 0. 
    //For example, if the attr is "Attack damage" and the val is "144":
       //Say the heuristic for attack damage is that it generally costs 1000 gold to get 30 damage This would equate
       //to a total investment of 144 * (1000 / 30)  = 4800 gold
       //Then assign a heuristic based on the gold investment value, maybe total estimated investment / 100
       //This would make the total heuristic value = round(4800 / 100) = 5 
    
	private static int heuristicForAttribute(String attr, Object val) {
        int typedVal = (Integer)val;
		//Get the heuristic multiplier for this attribute
		Double mult = PlayerStats.heuristicUnits.get(attr);
		return (int) Math.round(mult * typedVal / 100);
	}
	
}

