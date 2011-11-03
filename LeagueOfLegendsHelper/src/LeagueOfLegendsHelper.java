import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

public class LeagueOfLegendsHelper {

	public static HashMap<String, LoLItem> items = getFactBase();
	public static HashMap<String, String[]> itemTree = getItemTree();
	public static HashMap<String, String[]> characters = getCharacters();


	// Parse the CSV file into HashMap of items. These are the facts used by the
	// expert system.
	public static HashMap<String, LoLItem> getFactBase() {
		String csvPath = LeagueOfLegendsHelper.class.getResource(
				"LoL_Items_CSV.csv").getPath();

		try {
			BufferedReader in = new BufferedReader(new FileReader(csvPath));
			HashMap<String, LoLItem> ret = new HashMap<String, LoLItem>();

			String headerLine = in.readLine();
			String[] fieldNames = headerLine.split(",");

			
			
			String line;
			while ((line = in.readLine()) != null) {
				LoLItem item = new LoLItem();
				String[] fields = line.split(",");

				for(int i=0; i< fields.length; i ++) {
					if(fields[i] == null || fields[i].equals("")) {
						continue;
					}

					item.put(fieldNames[i], fields[i]);
				}

				ret.put((String) item.get(fieldNames[0]), item);
			}
			return ret;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		// Never gets here
		return null;

	}

	// Parse the CSV file into HashMap of items. This will be used to
	// reconstruct item trees.
	public static HashMap<String, String[]> getItemTree() {
		String csvPath = LeagueOfLegendsHelper.class.getResource(
				"LoL_Item_Tree.csv").getPath();

		try {
			BufferedReader in = new BufferedReader(new FileReader(csvPath));
			HashMap<String, String[]> ret = new HashMap<String, String[]>();

			// Read Past the header line
			in.readLine();

			String line;
			String parentName;
			// If the parent is the only item just add the parent. If the parent
			// has children
			// in the line then split the line by commas and add both to the
			// hashmap
			while ((line = in.readLine()) != null) {
				if (line.indexOf(',') == -1) {
					parentName = line;
					ret.put(parentName, null);
				} else {
					parentName = line.substring(0, line.indexOf(',', 0));
					line = line.substring(line.indexOf(',', 0) + 1);
					String[] childNames = line.split(",");

					ret.put(parentName, childNames);
				}
			}
			return ret;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		// Never gets here
		return null;

	}

	// Build Character Map
		public static HashMap<String, String[]> getCharacters() {
			String csvPath = LeagueOfLegendsHelper.class.getResource(
					"LoL_Characters.csv").getPath();

			try {
				BufferedReader in = new BufferedReader(new FileReader(csvPath));
				HashMap<String, String[]> ret = new HashMap<String, String[]>();

				String line;
                String characterName;
				while ((line = in.readLine()) != null) {
					
					    characterName = line.substring(0, line.indexOf(',', 0));
						line = line.substring(line.indexOf(',', 0) + 1);
						String[] attributes = line.split(",");

						ret.put(characterName, attributes);					
				}
				return ret;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}

			// Never gets here
			return null;

		}
		
	/**
	 * 
	 * @param Item
	 *            Name of the item to check for attribute.
	 * @param Attribute
	 *            Name of the attribute to check for.
	 * @return True if the attribute value is not empty.
	 */
	static boolean hasAttribute(String Item, String Attribute) {

	  // Return true if the item has the attribute and the attribute is not empty.
	  return items.get(Item).containsKey(Attribute);

	}

	static Vector<String> getAllChildren(String Item) {
		Vector<String> children = new Vector<String>();
		if (itemTree.get(Item) == null) {
			return null;
		}

		String[] Items = itemTree.get(Item);
		for (String item : Items) {
			children.add(item);
			String[] subItems = itemTree.get(item);
			if (subItems != null)
				for (String subItem : subItems) {
					children.add(subItem);
				}
		}
		return children;
	}

	/**
	 * 
	 * @param Attribute
	 *            Attribute to search all items for.
	 * @return List of Item names that have the requested Attribute
	 */
	static public List<String> getAttributeItems(String Attribute) {

		Iterator<Entry<String, LoLItem>> iter = items.entrySet().iterator();
		LinkedList<String> ret = new LinkedList<String>();

		String currentKey;

		while (iter.hasNext()) {
			currentKey = iter.next().getKey();

			if (hasAttribute(currentKey, Attribute)) {
				ret.add(currentKey);
			}
		}

		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		LinkedList<Token> tokList = new LinkedList<Token>();
		//tokList.add( new Token(Token.Typ.LogicalOp, "not"));
		tokList.add( new Token(Token.Typ.Player, Token.PlayerTyp.Them));
		tokList.add( new Token(Token.Typ.Attribute, "AbilityPower"));
		
		ItemRule r = Token.tokens2ItemRule(tokList);
		
		for(String s : items.keySet()) {
			LoLItem item = items.get(s);
			if(r.eval(item)) {
				System.out.println(item.get("Item"));
			}
		}
		
		System.out.println(r);
		
	    new UserInterface(items, characters, itemTree).showDialog();
		
		/*
		LinkedList<LoLItem> myItems = new LinkedList<LoLItem>();
		LinkedList<LoLItem> theirItems = new LinkedList<LoLItem>();
		
		myItems.add(items.get("Doran's Blade"));
		theirItems.add(items.get("Rabadon's Deathcap"));
		cd.put("playerItems", myItems);
		cd.put("opponentItems", theirItems);
		cd.put("playerChampionRole", Expert.ChampionRole.Fighter);
		cd.put("opponentChampionRole", Expert.ChampionRole.Mage);
		*/

		// CaseData cd = new CaseData();
		 //Expert.suggestNextItem(cd, items, itemTree);	
	}
}
