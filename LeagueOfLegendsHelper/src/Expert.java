import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class Expert {
	@SuppressWarnings("unchecked")
	static Answer suggestNextItem(CaseData caseData, HashMap<String, LoLItem> itemData, HashMap<String, String[]> itemHierarchy) {
		
		//Translate the currently possessed items of both players into statistical data. This will be used for heuristics later.
		PlayerStats playerStats = PlayerStats.calculateStats( (LinkedList<LoLItem>) caseData.get("playerItems"));
		PlayerStats opponentStats = PlayerStats.calculateStats((LinkedList<LoLItem>) caseData.get("opponentItems"));
		
		//Get the core attributes for each player's role		
		HashSet<String> playerCore =   coreAttributesFor( (ChampionRole) caseData.get("playerChampionRole"));
		HashSet<String> opponentCore = coreAttributesFor( (ChampionRole) caseData.get("opponentChampionRole"));
		
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
		
		//Apply exceptions here. Remove any disparity from the disparity list that we don't care about. 
		  //For instance, if the opponent is a support character, don't care about the ability power/cdr/magic pen VS magic resist disparity.
		  //Another example, if the opponent is a mage, don't care about the attack damage/attack speed/ crit chance VS armor disparity. 
		  //Another example, if the player is support, don't care about the ability power VS magic pen disparity.
		

		//If any disparateProperties still exist, try to find items that fit the player's core properties and
		//satisfy some of the disparateProperties.
		
		
		
		
		return null;
	}
 
	



	public class Answer {
	   String response;
	   LinkedList<String> items;
	}
	
	//Types of champion roles
	public enum ChampionRole { Assasin, Tank, Mage, Fighter, Support };
	
	
	//This function returns the core attributes for a type of champ; for example, a Fighter would have attack damage, armor, armor pen, health, etc
	public static HashSet<String> coreAttributesFor(ChampionRole c) {
		HashSet<String> attr = new HashSet<String>();
		if(c == ChampionRole.Assasin){
			attr.add("ArmorPenetration");
			attr.add("Critical");
			attr.add("Damage");						
		}else if(c == ChampionRole.Tank){
			attr.add("Armor");
			attr.add("Health");
			attr.add("MagicResist");
		}else if(c == ChampionRole.Mage){
			attr.add("AbilityPower");
			attr.add("MagicPenetration");
			attr.add("CDR");
			attr.add("SpellVamp");
		}else if(c == ChampionRole.Fighter){
			attr.add("AttackSpeed");
			attr.add("Critical");
			attr.add("Damage");
			attr.add("LifeSteal");	
		}else if(c == ChampionRole.Support){
			attr.add("AbilityPower");
			attr.add("CDR");
			attr.add("SpellVamp");			
		}
		return attr;
	}
	
	
    private static String counterFor(String attr) {

    	if(attr.equals("AbilityPower")) {
    		return "MagicResist";    		
    	}

    	else if(attr.equals("Critical")) {
    		return "Armor";
    	}
    	else if(attr.equals("AttackSpeed")) {
    		return "Armor";
    	}
    	else if(attr.equals("LifeSteal")) {
    		return "Armor";
    	}
    	else if(attr.equals("SpellVamp")) {
    		return "MagicResist";
    	}
    	else if(attr.equals("CDR")) {
    		return "MagicResist";
    	}

    	//There is no counter for this attribute.
    	return "";    	
    	
    }
    
    //This function will assign a value to an investment in an attribute. It should return a number greater than or equal to 0. 
    //For example, if the attr is "Attack damage" and the val is "144":
       //Say the heuristic for attack damage is that it generally costs 1000 gold to get 30 damage This would equate
       //to a total investment of 144 * (1000 / 30)  = 4800 gold
       //Then assign a heuristic based on the gold investment value, maybe total estimated investment / 100
       //This would make the total heuristic value = round(4800 / 100) = 5 
	private static int heuristicForAttribute(String attr, Object val) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

