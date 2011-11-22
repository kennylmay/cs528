package chatbot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import opennlp.tools.parser.Parse;

@SuppressWarnings("unchecked")
public class Responders {


	
	private String serialize(Parse[] p) {
		String ret = "";
		for(Parse c : p) {
			ret += c.getType() + ",";
		}
		return ret;
	}
	

	public String response(Parse parse) {
		Parse[] children = parse.getChildren();
		Parse p = children[0];
		System.out.println("p is " + p.getType());
		if(p.getType().equals("S")) {
			return S(p);
		}
		return "";
	}
	
	/******************************************************/
	/* Clause level responders                            */
	/******************************************************/
	
	private String S(Parse p) {
		//Response: So NP VP...

		if( serialize(p.getChildren()).equals("NP,VP,") ) {
		   String sent = flipPossesives(p.getChildren()[0].toString() + " " + p.getChildren()[1].toString()); 
		   Random rand = new Random();
		   int val = Math.abs(rand.nextInt() % 5);
		   switch(val) {
		   case 0 : return "So "  + sent + ", huh?"; 
		   case 1 : return "Why does it matter if "  + sent + "?"; 
		   case 2 : return "Let me get this straight, "  + sent + "?";
		   case 3 : return "Fascinating."; 
		   case 4 : return  sent + ".... Cool story bro."; 
		   }
		   
		}
		return  "";
	}

	private String SBAR(Parse p) {
		return "";
	}


	private String SBARQ(Parse p) {
		return "";
	}

	private String SINV(Parse p) {
		return "";
	}

	private String SQ(Parse p) {
		return "";
	}
	
	// depth first search on the parse tree that returns the first instance of
	// parse that is of type matching one of the strings in names
	static Parse findFirstTag(Parse tree, String[] names) {
		for (String s : names) {
			if (tree.getType().equals(s)) {
				return tree;
			}
		}

		for (Parse child : tree.getChildren()) {
			Parse p = findFirstTag(child, names);
			if (p != null) {
				return p;
			}
		}

		return null;
	}
	
	static String flipPossesives(String s) {

		s = s.toLowerCase();
		HashSet<String> compositeKeys = new HashSet<String>();
		for(String key : oppositeKeys) {
			if(compositeKeys.contains(key)) {
				continue;
			}
			
			if(!s.contains(key)) {
				continue;
			}
			

			s = myReplace(s, key, opposites.get(key));
    	
    		
    		for(String comp : opposites.get(key).split(" ") ) {
    			compositeKeys.add(comp);
    		}
    		
    		//Add the taboo key
    		compositeKeys.add(opposites.get(key));
    	
		}
		
		return s;

	}
	
	static String myReplace(String s, String key, String replace) {
		int loc = s.indexOf(key);
		
		while(loc != -1) {
			//Make sure that token does not exist inside a larger word

			if( 
					(loc-1 > 0 && Character.isLetter(s.charAt(loc-1))) ||
					(loc + key.length() < s.length() && Character.isLetter(s.charAt(loc + key.length())))
			) {
				//do nothing
			}
			else {
				s = s.replaceFirst(key, replace);
			}
			loc = s.indexOf(key, loc+1);
		}
		
		return s;
		
	}
	
	static LinkedList<String> oppositeKeys = new LinkedList<String>();
	static HashMap<String,String> opposites = new HashMap<String,String>();
    static {
    	opposites.put("i am", "you are");
    	opposites.put("i", "you");
    	opposites.put("me", "you");
    	opposites.put("my", "your");
    	
    	//Put in reverse order pairs
    	LinkedList<String> keys = new LinkedList<String>();
    	for(String key : opposites.keySet()) {
    		keys.add(key);
    	}
    	for(String key : keys) {
    		opposites.put(opposites.get(key),key);
    	}
    	for(String key: opposites.keySet()) {
    		oppositeKeys.add(key);
    	}
    	//Sort keys
    	Collections.sort(oppositeKeys, new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				if(arg0.length() < arg1.length()) return 1;
				else if(arg0.length() == arg1.length()) return 0;
				else return -1;
			}
    	});
    	
    }
	
}
