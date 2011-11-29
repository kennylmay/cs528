package chatbot;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.didion.jwnl.data.POS;

import opennlp.tools.parser.Parse;

@SuppressWarnings("unchecked")
public class Responders {

	WordRelations wr = new WordRelations();
	
	public class Entry {
			public String wordPattern = "";
			public String hypernymPattern = "";
			public POS pos;
			public Response r;
			public boolean wordMatchesSentence(Parse p) {
				if(wordPattern.length() == 0) {
					return false;
				}
				return p.toString().contains(wordPattern);
			}
			
			public boolean hypernymMatchesSentence(Parse p) {
				if(hypernymPattern.length() == 0) {
					return false;
				}
				
				if(pos == POS.NOUN) {
   				  //Get all tags for nouns
				  LinkedList<Parse> nouns = findAllTags(p,new String[] {"NN", "NNS"});
				
				  //Try each noun
				  for(Parse noun : nouns) {
					  System.out.println("Trying noun: " + noun + " hypermatched to : " + hypernymPattern + " with POS: " + pos);
					if(wr.isHypernymOf(noun.toString(), POS.NOUN, hypernymPattern)) {
						return true;
					}
			      }
				}
			 return false;
			}
			    
		
	}
	


	 
	
	private HashMap<String, LinkedList<Entry>> responseTables = buildResponseTables();
	private HashMap<String, Response> defaultResponses = buildDefaultResponses();
	private HashMap<String, LinkedList<Entry>> buildResponseTables() {
		HashMap<String, LinkedList<Entry>> ret = new HashMap<String, LinkedList<Entry>>();
		
		//build response tables..
		ret.put("S", S());
		//ret.put("SBAR", SBAR());
		//ret.put("SBAQR", SBARQ());
		//ret.put("SINV", SBARQ());
		//ret.put("SQ", SQ());
		return ret;
	}
	
	
	private HashMap<String, Response> buildDefaultResponses() {
		HashMap<String, Response> ret = new HashMap<String, Response>();
		
		//build default responses..
		ret.put("S", defaultS());
		//ret.put("SBAR", defaultSBAR());
		//ret.put("SBAQR", defaultSBARQ());
		//ret.put("SINV", defaultSBARQ());
		ret.put("SQ", defaultSQ());
		
		return ret;
	}
	
	

	

	public String response(Parse parse, HashMap<String,Object> context) {
		
		//Get the first non top entity
		Parse[] children = parse.getChildren();
		Parse p = children[0];
		System.out.println("p is " + p.getType());
			
		
		//Put the user sentence into context structure.. it might be useful later
		if(!context.containsKey("userSentences")) {
			context.put("userSentences", new HashMap<String,Integer>());
		}
		incrementKey( ((HashMap<String,Integer>)context.get("userSentences")), p.toString());
		
		
		//Get the response table that corresponds to this "top level" parse
		LinkedList<Entry> tableOfInterest = responseTables.get(p.getType());
		
		//If no table, do a generic response
		if(tableOfInterest == null) {
			return "Pardon me?";
		}
		
		//Scan the table of interest for matches
		for(Entry ent : tableOfInterest ) {
			if(ent.hypernymMatchesSentence(p)) {
				return ent.r.response(p,context);
			}
		}
		
		//Scan the table of interest for matches
		for(Entry ent : tableOfInterest ) {
			if(ent.wordMatchesSentence(p)) {
				return ent.r.response(p,context);
			}
		}
		//If no matches, use the default
		Response defaultResponse = defaultResponses.get(p.getType());
		if(defaultResponse == null) {
			System.out.println("Bug! no default response for: " + p.getType());
		}
		else {
			return defaultResponse.response(p, context);
		}
		
		//Shouldn't get here
		return "Bug!";
		
	}
	



	/******************************************************/
	/* Clause level responders                            */
	/******************************************************/
	

	//Make the default responder for S. This is the "fall through" response that
	//is invoked if no matches are found.
	private static Response defaultS() {
		Response defaultResponse = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				//Flip possessive statements
			    String sent = flipPossesives(p.toString());
			    
			    //Put this into the statements context.. it might be useful later
			    if(!context.containsKey("statements")) {
			    	context.put("statements", new LinkedList<String>());
			    }
			    ((LinkedList<String>)context.get("statements")).add(sent); 
			    			    
				Random rand = new Random();
				int val = Math.abs(rand.nextInt() % 5);
				switch(val) {
				case 0 : return "So "  + sent + ", huh?"; 
				case 1 : return "Why does it matter if "  + sent + "?"; 
				case 2 : return "Let me get this straight, "  + sent + "?";
				case 3 : return "Fascinating."; 
				case 4 : return  sent + ".... Cool story bro.";
				default : return "Bug!";
			    }
		     }
	    };
	    return defaultResponse;
	}
	
	private static Response defaultSQ() {
		Response defaultResponse = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				//Flip possessive statements
			    String sent = flipPossesives(p.toString());
			    
			    //Put this into the statements context.. it might be useful later
			    if(!context.containsKey("directQuestions")) {
			    	context.put("directQuestions", new LinkedList<String>());
			    }
			    ((LinkedList<String>)context.get("directQuestions")).add(sent); 
			    			    
				Random rand = new Random();
				int val = Math.abs(rand.nextInt() % 5);
				switch(val) {
				case 0 : return "Yes"; 
				case 1 : return "No."; 
				case 2 : return "Negatory.";
				case 3 : return "Heck yeah!"; 
				case 4 : return  "Hmmmm..... No.";
				default : return "Bug!";
			    }
		     }
	    };
	    return defaultResponse;
	}
	
	
	//Return the response actions for S type parses
	private LinkedList<Entry> S() {
		LinkedList<Entry> ret = new LinkedList<Entry>();
		
		//Add response actions here...
		
		//Example: some basic responses. Basic responses just return a canned string.
		ret.add( makeWordMatchEntry(new BasicResponse("I love dogs. They can be a pain in the butt sometimes though!"), "dog"));
		ret.add( makeHyperMatchEntry(new BasicResponse("You're making me hungry!"), "food", POS.NOUN));
		return ret;
		
	}

	
	

	
	/******************************************************/
	/* Helper functions, classes, structures              */
	/******************************************************/
	private static Responders inst = new Responders();
	public Entry makeWordMatchEntry(Response r, String wordPattern) {
		Entry ret = new Entry();
		ret.r = r;
		ret.wordPattern = wordPattern;
		return ret;
	}
	
	public Entry makeHyperMatchEntry(Response r, String hyperPattern, POS pos) {
		Entry ret = new Entry();
		ret.r = r;
		ret.hypernymPattern = hyperPattern;
		ret.pos = pos;
		return ret;
	}
	
	//Basic response --- Just returns a string of text.
	public class BasicResponse implements Response {

		private final String dumbText;
		
		public BasicResponse(String text) {
			dumbText = text;
		}
		
		@Override
		public String response(Parse p, HashMap<String, Object> context) {
			
			//If this is an "S" type, pick up this simple clause as a statement
			if(p.getType().equals("S")) {
			    if(!context.containsKey("statements")) {
			    	context.put("statements", new LinkedList<String>());
			    }
			    ((LinkedList<String>)context.get("statements")).add(flipPossesives(p.toString())); 
			}
			return dumbText;
		}
		
	};
	
	//Increments a key in a hashmap<string,int>
	private void incrementKey(HashMap<String, Integer> hashMap, String key) {
		if(hashMap.containsKey(key)) {
			Integer val = hashMap.get(key);
			val++;
			hashMap.put(key, val);
		}
		else {
			hashMap.put(key, new Integer(1));
		}
		
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
	
	static LinkedList<Parse> findAllTags(Parse tree, String[] names) {
		LinkedList<Parse> list = new LinkedList<Parse>();
		
		LinkedList<Parse> workQ = new LinkedList<Parse>();
		workQ.add(tree);
		
		//Walk the entire tree looking for matching tags
		while(workQ.size() > 0) {
			Parse p = workQ.poll();
			
			for (String s : names) {
				if (p.getType().equals(s)) {
					list.add(p);
				}
			}
			
			for(Parse child : p.getChildren()){
				workQ.add(child);
			}
		}
		
		return list;
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

    	//Get the max bite
    	for(String key : sortedByLargestFirst(oppositeKeys)) {
    		maxBite = Math.max(maxBite, key.split(" ").length);
    	}
    	
    }
	
	private String serialize(Parse[] p) {
		String ret = "";
		// Construct the entry;
		for (Parse child : p) {
		   ret += child.getType() + " ";
		}
         ret = ret.trim();
		return ret;
	}
	
	private static Collection<String> sortedByLargestFirst(Collection<String> col) {
		LinkedList<String> ret = new LinkedList<String>();
		
		//Copy out the collection
		for(String s : col) {
			ret.add(s);
		}
		//Sort by ascending
    	//Sort keys by length descending
    	Collections.sort(ret, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				if(arg0.length() < arg1.length()) return 1;
				else if(arg0.length() == arg1.length()) return 0;
				else return -1;
			}
    	});
		
		return ret;
	}
}
