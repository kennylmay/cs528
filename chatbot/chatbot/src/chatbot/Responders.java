package chatbot;

import java.util.HashMap;
import java.util.HashSet;
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
		   String sent = flipPossesives(p.getChildren()[0].toString()) + " " + flipPossesives(p.getChildren()[1].toString()); 
		   Random rand = new Random();
		   int val = rand.nextInt() % 5;
		   switch(val) {
		   case 0 : return "So "  + sent + ", huh?"; 
		   case 1 : return "Why does it matter if "  + sent + "?"; 
		   case 2 : return "Let me get this straight, "  + sent + "?";
		   case 3 : return "Fascinating.";
		   case 4 : return p.getChildren()[0] + sent + ".... Cool story bro."; 
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
		String ret = "";
		String[] toks = s.split(" ");
		for(String tok : toks) {
		  if(opposites.get(tok) != null) {
			  ret += opposites.get(tok)  + " ";
		  }
		  else {
			  ret += tok + " ";
		  }
		}
		
		return ret.substring(0, ret.length()-1);
		
	}
	
	
	static HashMap<String,String> opposites = new HashMap<String,String>();
    static {
    	opposites.put("i", "you");
    	opposites.put("I", "You");
    	opposites.put("me", "you");
    	opposites.put("my", "your");
    	
    	//Put in reverse order pairs 
    	HashSet<String> keys = (HashSet<String>) opposites.keySet();
    	keys = (HashSet<String>) keys.clone();
    	for(String key : keys) {
    		opposites.put(opposites.get(key),key);
    	}
    }
	
}
