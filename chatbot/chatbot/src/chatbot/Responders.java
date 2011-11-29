package chatbot;

import java.util.Arrays;
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
		Global.incrementKey( ((HashMap<String,Integer>)context.get("userSentences")), p.toString());
		
		
		//Get the response table that corresponds to this "top level" parse
		LinkedList<Entry> tableOfInterest = responseTables.get(p.getType());
		
		//If no table, do a generic response
		if(tableOfInterest == null) {
			return "Pardon me?";
		}
		
		//Scan the table of interest for matches
		int maxScore = 0;
		Entry bestMatch = null;
		for(Entry ent : tableOfInterest ) {
		//	if(ent.hypernymMatchesSentence(p)) {
		//		return ent.r.response(p,context);
		//	}
		    int tmp = ent.topHyperMatchInSentence(p);
		    if(tmp > maxScore) {
		    	bestMatch = ent;
		    }
		}
		if(bestMatch != null) {
			return bestMatch.r.response(p, context);
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
	/* Response Actions                                    */
	/******************************************************/
	//Make the default responder for S. This is the "fall through" response that
	//is invoked if no matches are found.
	private static Response defaultS() {
		Response defaultResponse = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				//Flip possessive statements
			    String sent = Global.flipPossesives(p.toString());
			    
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
			    String sent = Global.flipPossesives(p.toString());
			    
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
		ret.add( makeHyperMatchEntry(new BasicResponse("I really don't care about living things...I'm a machine!"), "animal", POS.NOUN));
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
			    ((LinkedList<String>)context.get("statements")).add(Global.flipPossesives(p.toString())); 
			}
			return dumbText;
		}
		
	}
	

}
