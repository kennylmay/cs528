import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

public class LeagueOfLegendsHelper {

	public static HashMap<String, LoLItem> items = getFactBase();
	public static HashMap<String, String[]> itemTree = getItemTree();
	
	// Cost for one unit of each attribute.
	public static HashMap<String, Object> attrCost = getAttrCosts();

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

	private static HashMap<String, Object> getAttrCosts() {

		HashMap<String, Object> ret = new HashMap<String, Object>();
		LoLItem tmpItem = null;
		
		double costSum = 0;
		double pointSum = 0;
		double costPerPoint = 0;
		
		// Heath - Using Basic items Ruby Crystal & Giants Belt
		tmpItem = items.get("Ruby Crystal");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Health").toString());
		
		tmpItem = items.get("Giant's Belt");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Health").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("Health", costPerPoint);
		
		
		// Mana - Using Sapphire Crystal for Base Item
		tmpItem = items.get("Sapphire Crystal");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Mana").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("Mana", costPerPoint);
		
		
		// Health Regen
		tmpItem = items.get("Rejuvenation Bead");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("HealthRegen").toString());
		
		tmpItem = items.get("Regrowth Pendant");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("HealthRegen").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("HealthRegen", costPerPoint);
		
		
		// Mana Regen
		tmpItem = items.get("Faerie Charm");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("ManaRegen").toString());
		
		tmpItem = items.get("Meki Pendant");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("ManaRegen").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("ManaRegen", costPerPoint);
		
		// Armor
		tmpItem = items.get("Chain Vest");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Armor").toString());
		
		tmpItem = items.get("Cloth Armor");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Armor").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("Armor", costPerPoint);
		
		
		// Magic Resist
		tmpItem = items.get("Null-Magic Mantle");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Resist").toString());
		
		tmpItem = items.get("Negatron Cloak");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Resist").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("Resist", costPerPoint);

		
		// Attack Damage
		tmpItem = items.get("B. F. Sword");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Damage").toString());
		
		tmpItem = items.get("Long Sword");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Damage").toString());
		
		tmpItem = items.get("Pickaxe");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Damage").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("Damage", costPerPoint);
		
		
		// Ability Power
		tmpItem = items.get("Amplifying Tome");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("AbilityPower").toString());

		tmpItem = items.get("Blasting Wand");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("AbilityPower").toString());
		
		tmpItem = items.get("Needlessly Large Rod");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("AbilityPower").toString());
		
		costPerPoint = costSum / pointSum;
		ret.put("AbilityPower", costPerPoint);
		
		
		// Critical Strike
		tmpItem = items.get("Brawler's Gloves");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Critical").toString());

		tmpItem = items.get("Cloak of Agility");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("Critical").toString());

		costPerPoint = costSum / pointSum;
		ret.put("Critical", costPerPoint);
		
		
		// Attack Speed
		tmpItem = items.get("Dagger");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("AttackSpeed").toString());

		tmpItem = items.get("Recurve Bow");
		costSum += Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum += Integer.getInteger(tmpItem.get("AttackSpeed").toString());

		costPerPoint = costSum / pointSum;
		ret.put("AttackSpeed", costPerPoint);
		
		
		//Life Steal
		tmpItem = items.get("Vampiric Scepter");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Lifesteal").toString());

		costPerPoint = costSum / pointSum;
		ret.put("Lifesteal", costPerPoint);
		
		
		// Spell Vamp
		tmpItem = items.get("Hextech Revolver");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("SpellVamp").toString());

		costPerPoint = costSum / pointSum;
		ret.put("SpellVamp", costPerPoint);
		
		// Dodge
		tmpItem = items.get("Ninja Tabi");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Dodge").toString());

		costPerPoint = costSum / pointSum;
		ret.put("Dodge", costPerPoint);

		
		// Cool Down Reduction
		tmpItem = items.get("Ionian Boots of Lucidity");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("CDR").toString());

		costPerPoint = costSum / pointSum;
		ret.put("CDR", costPerPoint);

		
		// Tenacity
		tmpItem = items.get("Moonflair Spellblade");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Tenacity").toString());

		costPerPoint = costSum / pointSum;
		ret.put("Tenacity", costPerPoint);
		
		// Movement
		tmpItem = items.get("Boots of Speed");
		costSum = Integer.getInteger(tmpItem.get("Cost").toString());
		pointSum = Integer.getInteger(tmpItem.get("Movement").toString());

		costPerPoint = costSum / pointSum;
		ret.put("Movement", costPerPoint);
		
		return ret;
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
	  return !items.get(Item).containsKey(Attribute);

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

		LanguageProcessor user = new LanguageProcessor(items);
	//	user.askQuestion();
		
		//ItemRules rules = new ItemRules();

		CaseData cd = new CaseData();
		LinkedList<LoLItem> tst = new LinkedList<LoLItem>();
		for(String str : items.keySet()) {
			tst.add(items.get(str));
		}
		
		cd.put("playerItems", tst);
		cd.put("opponentItems", tst);
		cd.put("playerChampionRole", Expert.ChampionRole.Assasin);
		cd.put("opponentChampionRole", Expert.ChampionRole.Mage);
		

		Expert.suggestNextItem(cd, items, itemTree);
		
	}
}
