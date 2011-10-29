import java.util.HashMap;
import java.util.LinkedList;


public class Expert {
	@SuppressWarnings("unchecked")
	static Answer suggestNextItem(CaseData caseData, HashMap<String, LoLItem> itemData, HashMap<String, String[]> itemHierarchy) {
		
		//Translate the current facts into statistical data. This can be used for heuristics later.
		PlayerStats playerStats = PlayerStats.calculateStats( (LinkedList<LoLItem>) caseData.get("playerItems"));
		PlayerStats opponentStats = PlayerStats.calculateStats((LinkedList<LoLItem>) caseData.get("opponentItems"));
		


		
		

		
		
		
		
		return null;
	}
 
	
	public class Answer {
	   String response;
	   LinkedList<String> items;
	}
	
	//Types of champion roles
	public enum ChampionRole { Assasin, Tank, Mage, Fighter, Support };
	
	
	
}

