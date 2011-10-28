import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;



public class LeagueOfLegendsHelper {


	public static HashMap<String, LoLItem> items = getFactBase();
	
	
	//Parse the CSV file into HashMap of items. These are the facts used by the expert system.
	public static HashMap<String, LoLItem>  getFactBase() {
		String csvPath = LeagueOfLegendsHelper.class.getResource("LoL_Items_CSV.csv").getPath();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(csvPath));
			HashMap<String, LoLItem> ret = new HashMap<String, LoLItem>();
		
			String headerLine = in.readLine();
			String[] fieldNames = headerLine.split(",");
			
			
			String line;
			while( (line = in.readLine()) != null) {
				LoLItem item = new LoLItem();
				String[] fields = line.split(",");
				for(int i=0; i< fields.length; i ++) {
			//		System.out.print(fieldNames[i] + " is " + fields[i] + ", ");
					item.put(fieldNames[i], fields[i]);
				}
				System.out.println("");
				
				//The name of the item is field 0
				ret.put((String)item.get(fieldNames[0]), item);
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
		
		//Never gets here
		return null;
		
	}
	
	/**
	 * 
	 * @param Item Name of the item to check for attribute.
	 * @param Attribute Name of the attribute to check for.
	 * @return True if the attribute value is not empty. 
	 */
	static boolean hasAttribute(String Item, String Attribute){

	  // Return true if the item has the attribute and the attribute is not empty.
	  return !items.get(Item).get(Attribute).toString().isEmpty();

	}
	
	/**
	 * 
	 * @param Attribute Attribute to search all items for.
	 * @return List of Item names that have the requested Attribute
	 */
	static public List<String> getAttributeItems(String Attribute) {
		
		Iterator<Entry <String, LoLItem>> iter = items.entrySet().iterator();
		LinkedList<String> ret = new LinkedList<String>();
		
		String currentKey;
		
		
		while(iter.hasNext()) {
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
		System.out.println(hasAttribute("Long Sword", "Damage"));
		System.out.println(hasAttribute("Phantom Dancer", "Damage"));
		
		System.out.println(getAttributeItems("Damage").toString());
	}

}
