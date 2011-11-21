package chatbot;

import java.util.HashMap;
import java.util.Random;

import opennlp.tools.parser.Parse;

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
		   Random rand = new Random();
		   int val = rand.nextInt() % 5;
		   switch(val) {
		   case 0 : return "So "  + p.getChildren()[0] + " " + p.getChildren()[1]+ ", huh?"; 
		   case 1 : return "Why does it matter if "  + p.getChildren()[0] + " " + p.getChildren()[1]+ "?"; 
		   case 2 : return "Let me get this straight, "  + p.getChildren()[0] + " " + p.getChildren()[1]+ "?";
		   case 3 : return "Fascinating.";
		   case 4 : return p.getChildren()[0] + " " + p.getChildren()[1]+ ".... Cool story bro."; 
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
	
}
