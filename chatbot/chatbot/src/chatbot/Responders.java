package chatbot;

import java.util.HashMap;

import opennlp.tools.parser.Parse;

public class Responders {


	
	private String serialize(Parse[] p) {
		String ret = "";
		for(Parse c : p) {
			ret += c.getType() + ",";
		}
		return ret;
	}
	

	
	/******************************************************/
	/* Clause level responders                            */
	/******************************************************/
	
	private String S(Parse p) {
		Parse[] children = p.getChildren();
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
