package chatbot;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import opennlp.tools.parser.Parse;

@SuppressWarnings("unchecked")
public class Responders {


	
	private String serialize(Parse[] p) {
		String ret = "";
		// Construct the entry;
		for (Parse child : p) {
		   ret += child.getType() + " ";
		}
         ret = ret.trim();
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
	    String sent = flipPossesives(p.toString()); 
		Random rand = new Random();
		int val = Math.abs(rand.nextInt() % 5);
		switch(val) {
		case 0 : return "So "  + sent + ", huh?"; 
		case 1 : return "Why does it matter if "  + sent + "?"; 
		case 2 : return "Let me get this straight, "  + sent + "?";
		case 3 : return "Fascinating."; 
		case 4 : return  sent + ".... Cool story bro."; 
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
		String[] toks = s.split(" ");
		LinkedList<String> tokens = new LinkedList<String>();
		for(String tok : toks) {
			tokens.add(tok);
		}
		
		LinkedList<String> retTokens = new LinkedList<String>();

		while(tokens.size() > 0) {
			int b;
			//Take bites from max to 0
			Bites:
			for(b = Math.min(maxBite, tokens.size()); b > 0; b--) {
				String bite = "";
				for(int i=0; i<b; i++) {
					bite += tokens.get(i) + " ";
				}
				bite = bite.substring(0, bite.length()-1);
				
				//Is this bite in opposites?
				if(opposites.get(bite) != null) {
					//If it is, add it as a ret token and remove the bites
					retTokens.add(opposites.get(bite));
					for(int j=0; j<b; j++) {
						tokens.poll();
					}
					break Bites;
				}
			}
			
			//If b is zero, no match was found. Add the token to ret
			if(b == 0) {
				retTokens.add(tokens.pollFirst());
			}
		}

		//Rebuild ret string
		String ret = "";
		for(String tok : retTokens) {
			ret += tok + " ";
		}

		return ret;
	}

	
	static LinkedList<String> oppositeKeys = new LinkedList<String>();
	static int maxBite = 0;
	static HashMap<String,String> opposites = new HashMap<String,String>();
    static {
    	opposites.put("i am", "you are");
    	opposites.put("i", "you");
    	opposites.put("you", "me");
    	opposites.put("my", "your");
    	opposites.put("mine", "yours");
    	opposites.put("you are", "i am");
    	
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
    	//Sort keys by length descending
    	Collections.sort(oppositeKeys, new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				if(arg0.length() < arg1.length()) return 1;
				else if(arg0.length() == arg1.length()) return 0;
				else return -1;
			}
    	});
    	
    	//Get the max bite
    	for(String key : oppositeKeys) {
    		maxBite = Math.max(maxBite, key.split(" ").length);
    	}
    	
    }
	
}
