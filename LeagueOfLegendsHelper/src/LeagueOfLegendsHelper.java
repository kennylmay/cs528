import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class LeagueOfLegendsHelper {


	public static HashMap<String, HashMap<String, Object>> items = getFactBase();
	
	
	//Parse the CSV file into HashMap of items. These are the facts used by the expert system.
	public static HashMap<String, HashMap<String, Object>>  getFactBase() {
		String csvPath = LeagueOfLegendsHelper.class.getResource("LoL_Items_CSV.csv").getPath();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(csvPath));
			HashMap<String, HashMap<String, Object>> ret = new HashMap<String, HashMap<String, Object>>();
		
			String headerLine = in.readLine();
			String[] fieldNames = headerLine.split(",");
			
			
			String line;
			while( (line = in.readLine()) != null) {
				HashMap<String, Object> item = new HashMap<String,Object>();
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
	
	static boolean hasAttribute(String Item, String Attribute){
	  HashMap<String, Object> ret = new HashMap<String, Object>();
	  ret = items.get(Item);
	  Object obj = ret.get(Attribute); 
	  String atts = obj.toString();
	  
	  if (atts != null){
		  System.out.println("|" + atts + "|");
		  return true;
	  }
	  
	  return false;

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(hasAttribute("Long Sword", "Damage"));
		System.out.println(hasAttribute("Phantom Dancer", "Damage"));
	}

}
