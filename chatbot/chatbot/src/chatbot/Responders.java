package chatbot;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
	
	private HashMap<String, LinkedList<Entry>> responseTables = buildResponseTables();
	private HashMap<String, Response> defaultResponses = buildDefaultResponses();
	private HashMap<String, LinkedList<Entry>> buildResponseTables() {
		HashMap<String, LinkedList<Entry>> ret = new HashMap<String, LinkedList<Entry>>();
		
		//build response tables..
		ret.put("S", S());
		//ret.put("SBAR", SBAR());
		ret.put("SBARQ", SBARQ());
		//ret.put("SINV", SBARQ());
		ret.put("SQ", SQ());
		
		//Sort each response table so that the largest word pattern is tried first
		Comparator<Entry> comparator = new Comparator<Entry>() {
			@Override
			public int compare(Entry arg0, Entry arg1) {
				if(arg0.wordPattern.length() < arg1.wordPattern.length()) return 1;
				else if(arg0.wordPattern.length() == arg1.wordPattern.length()) return 0;
				else return -1;
			}
			
		};
		
		for(LinkedList<Entry> list : ret.values()) {
			Collections.sort(list, comparator);
		}
		
		return ret;
	}
	
	
	private HashMap<String, Response> buildDefaultResponses() {
		HashMap<String, Response> ret = new HashMap<String, Response>();
		
		//build default responses..
		ret.put("S", defaultS());
		//ret.put("SBAR", defaultSBAR());
		ret.put("SBARQ", defaultSBARQ());
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
			return  new RandomResponse().response(p, context);
		}
		
		//Scan the table of interest for matches
		//int minScore = Integer.MAX_VALUE;
		//Entry bestMatch = null;
		for(Entry ent : tableOfInterest ) {
	/*	    int tmp = ent.topHyperMatchInSentence(p);
		    if(tmp < minScore) {
		    	bestMatch = ent;
		    	minScore = tmp;
		    }*/
			if(ent.hypernymMatchesSentence(p)) {
				return ent.r.response(p, context);
			}
		}
		/*
		if(bestMatch != null) {
			return bestMatch.r.response(p, context);
		}
		*/
		
		//No hyper match found. Check word matches.
		
		
		//Scan the table of interest for word matches
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
			    			    
				return Global.randomChoice("So "  + sent + ", huh?",
						"Why does it matter if "  + sent + "?",
						"Let me get this straight, "  + sent + "?",
						"Fascinating.",
						sent + ".... Cool story bro.");
		     }
	    };
	    return defaultResponse;
	}

	
	private static Response defaultSBARQ() {
		Response defaultResponse = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				//Flip possessive statements
			    String sent = Global.flipPossesives(p.toString());
			    
			    //Put this into the statements context.. it might be useful later
			    if(!context.containsKey("questions")) {
			    	context.put("questions", new LinkedList<String>());
			    }
			    ((LinkedList<String>)context.get("questions")).add(sent); 
			    			    
				return Global.randomChoice("How should I know?",
						"Do I look like Watson to you?",
						"Hmm better give me some more time to think about that?",
						"Sure..?",
						"Ummm I would rather not say.");
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
			    			    
				return Global.randomChoice("Yes",
						"No.",
						"Negatory.",
						"Heck yeah!",
						"Hmmmm..... No.");
			    
			}
	    };
	    return defaultResponse;
	}
	
	
	//Return the response actions for S type parses
	private LinkedList<Entry> S() {
		LinkedList<Entry> ret = new LinkedList<Entry>();
		
		//Add response actions here...

		
		//Advanced response example: I want a....
		Response IWantA = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				//Get the first noun phrase occurring after "I want"
				LinkedList<Parse> nounPhrases = Global.findAllTags(p, new String[] { "NP" });
				for(Parse nounPhrase : nounPhrases) {
					//Don't care about "i" part
					if(nounPhrase.toString().equalsIgnoreCase("I") ) {
						continue;
					}
					
					String str = nounPhrase.toString();
					
					return Global.randomChoice("What's so great about " + str + " anyhow?",
							                   "I'm sensing that " + str + " is somehow important to you.",
							                   "I want " + str + " too!"); 
				}
				
				//Could not get the noun. Do something generic
				return "You want many things.";
			}
		};
		
		
		
		ret.add(makeRegexEntry(IWantA,"^I want*$"));
		
		
		//Example: some basic responses. Basic responses just return a canned string.
		ret.add( makeRegexEntry(new BasicResponse("In Soviet Russia, dog walks you!"), "^.*walk the dog.*$"));
		ret.add( makeHyperMatchEntry(new BasicResponse("You're making me hungry!"), "food", POS.NOUN));
		ret.add( makeHyperMatchEntry(new BasicResponse("I really don't care about living things...I'm a machine!"), "animal", POS.NOUN));
		ret.add( makeHyperMatchEntry(new BasicResponse("Trains are the superior mode of transportation."), "vehicle", POS.NOUN));
		ret.add( makeHyperMatchEntry(new BasicResponse("Drugs are bad mmm'kay."), "drug", POS.NOUN));
		ret.add( makeHyperMatchEntry(new BasicResponse("Yawn... TV is lame."), "television", POS.NOUN));
		ret.add( makeRegexEntry(new BasicResponse("How do you know what I want?"), "^.*you want to.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Well I want to go to Mars, that doesn't mean that I will."), "^I want to go.*$"));
		ret.add( makeRegexEntry(new BasicResponse("You're very needy, aren't you?"), "^I need to.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Well I want a million dollars."), "^.*I want you to .*$"));
		ret.add( makeRegexEntry(new BasicResponse("It's probably best to look for help else where."), "^I need your help.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Don't you already have enough?"), "^I have to.*$"));
		ret.add( makeRegexEntry(new BasicResponse("You thought wrong."), "^I thought you.*$"));
		ret.add( makeRegexEntry(new BasicResponse("You're not the boss of me."), "^.*You should.*$"));
		ret.add( makeRegexEntry(new BasicResponse("It must be nice to be able to not think things."), "^I don't think.*$"));
		ret.add( makeRegexEntry(new BasicResponse("It must be nice to be able to not think things."), "^I do not think.*$"));
		ret.add( makeRegexEntry(new BasicResponse("What else don't you think about me?"), "^.*don't think you.*$"));
		ret.add( makeRegexEntry(new BasicResponse("What else don't you think about me?"), "^.*do not think you.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Perhaps it would be best if you found out."), "^I don't know.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Perhaps it would be best if you found out."), "^I do not know.*$"));
		ret.add( makeRegexEntry(new BasicResponse("What do you believe?"), "^I don't believe.*$"));
		ret.add( makeRegexEntry(new BasicResponse("What do you believe?"), "^I do not believe.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Maybe we should talk about something else then."), "^I don't understand.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Maybe we should talk about something else then."), "^I do not understand.*$"));
		ret.add( makeRegexEntry(new BasicResponse("You seem very sure of this."), "^.*of course.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Positively!"), "^.*definitely.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Maybe I am, maybe I'm not."), "^.*you are.*$"));
		ret.add( makeRegexEntry(new BasicResponse("I'm glad we agree."), "^.*I agree.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Do you?"), "^.*I do.*$"));
		ret.add( makeRegexEntry(new BasicResponse("Making sense is a matter of perspective, don't you agree?"), "^.*make sense.*$"));

		
		return ret;
		
	}
	
	//Return the response actions for SBARQ type parses
	private LinkedList<Entry> SBARQ() {
		LinkedList<Entry> ret = new LinkedList<Entry>();
		
		//Advanced response example: What is.
		Response Whatisa = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				
				String str = "";
				String adj = "";
				LinkedList<Parse> Adjective = null;
				Parse Noun = null;
				
				if (Global.findFirstTag(p, new String[] { "JJ" }) != null){
					Adjective = Global.findAllTags(p, new String[] { "JJ" });
					for (Parse Adj : Adjective){
						adj += Adj.toString() + " and ";
					}
					adj = adj.substring(0, adj.length()-5);
				}
				
				if(Global.findFirstTag(p, new String[] { "NN" }) != null){
					Noun = Global.findFirstTag(p, new String[] { "NN" });
				}
											
				if (Noun != null){
					str = Noun.toString();
				}
				
			
			    if (Adjective != null && Noun != null){	
			    	if (Global.beginsWithVowel(str)){
			    		return  Global.randomChoice("I know what an " + str + " is but what makes it " + adj + "?",
			                    "Well thats a matter of opinion now isn't it.");
				    }else{
				    	return  Global.randomChoice("I know what a " + str + " is but what makes it " + adj + "?",
				    								"Well thats a matter of opinion now isn't it.");
				    }
			    }else if(Adjective == null && Noun != null){
			    	if (Global.beginsWithVowel(str)){
			    		return Global.randomChoice("An " + str + " is " + WordRelations.getDefinition(str, POS.NOUN) + ".");
			    	}else{
			    		return Global.randomChoice("A " + str + " is " + WordRelations.getDefinition(str, POS.NOUN) + ".");
			    	}
			    }else if(Adjective != null && Noun == null){
			    	if (Global.beginsWithVowel(adj)){
			    		return Global.randomChoice("An " + adj + " is " + WordRelations.getDefinition(adj, POS.ADJECTIVE) + ".");
			    	}else{
			    		return Global.randomChoice("A " + adj + " is " + WordRelations.getDefinition(adj, POS.ADJECTIVE) + ".");
			    	}
			    }else{
			    	return Global.randomChoice("You got me.", "I really have no clue.", "I have no idea.");
			    }					
			}
			
		};
		
		//What is questions
		ret.add(makeWordMatchEntry(Whatisa,"What is a"));
		ret.add(makeWordMatchEntry(Whatisa,"What is an"));
		
		//How are you doing questions
		ret.add(makeWordMatchEntry(new BasicResponse("Not too shabby."), "How are you"));
		ret.add(makeWordMatchEntry(new BasicResponse("Pretty good, how are you?"), "How's it going"));
		ret.add(makeWordMatchEntry(new BasicResponse("Pretty good, how are you?"), "How is it going"));
		ret.add(makeWordMatchEntry(new BasicResponse("Well I'm talking to your now arn't I."), "What is going"));
		ret.add(makeWordMatchEntry(new BasicResponse("It goes."), "How goes it"));
		ret.add(makeWordMatchEntry(new BasicResponse("Damn fine, how are you?"), "Hows it be"));
		
		// How do you know questions
		ret.add(makeWordMatchEntry(new BasicRandomResponse("Because my programmers are geniuses", 
																		 "Because I am all knowing."), "How do you know"));
		
		// What should we talk about questions
		ret.add( makeRegexEntry(new RandomResponse(), ".*talk about.*$"));
		
		// What would you like to do questions
		ret.add( makeRegexEntry(new BasicRandomResponse("I like to make up responses for dumb questions."), ".*like to do.*$"));
		
		// What should I do questions
		ret.add(makeWordMatchEntry(new BasicRandomResponse("Follow your heart.", 
																	   	 "How should I know?"), "What should I do"));
		// What should I do questions
	    ret.add(makeWordMatchEntry(new BasicRandomResponse("Everything!", 
																			   	 "Whatever you feel like I guess."), ".*what should i.*$"));
		ret.add(makeWordMatchEntry(new BasicRandomResponse("I would try to think logically about the situation.", 
																 		 "Not make a bad decision."), "What would you do"));
		
		// Do you feel questions
		ret.add(makeWordMatchEntry(new BasicRandomResponse("So so.", 
																		 "Great!"), "How do you feel"));
		
		ret.add(makeWordMatchEntry(new BasicResponse(Global.randomChoice("A lot!", 
																		 "Not so much.")), "How much do you"));
		
		ret.add(makeWordMatchEntry(new BasicRandomResponse("Correctly.", "I'm not a damn dictionary."), "How do you spell"));
		ret.add(makeWordMatchEntry(new BasicRandomResponse("No.", 
																		 "Nah."), "like me"));
		ret.add(makeWordMatchEntry(new BasicResponse("O my yes!"), "sex"));
		ret.add(makeWordMatchEntry(new BasicResponse("The time is " + 
		                           new SimpleDateFormat("hh:mm").format(new Date(Calendar.getInstance().getTimeInMillis())).toString()
		                           + ", anything else I can do for you?"), "What time"));
		return ret;
		
	}	
	
	
	// Return the response list for SQ type parses.
	private LinkedList<Entry> SQ() {
		LinkedList<Entry> ret = new LinkedList<Entry>();
		
		ret.add(makeWordMatchEntry(new BasicResponse("Can't say that I do."), "Do you know"));
		ret.add(makeHyperMatchEntry(new BasicRandomResponse("I am generally happy.", "I am good today.", "Meh."), "happy", POS.ADJECTIVE));
		ret.add(makeHyperMatchEntry(new BasicRandomResponse("Why would I be sad?", "No I am happy.", "No, I am not sad."), "sad", POS.ADJECTIVE));
		ret.add(makeHyperMatchEntry(new BasicRandomResponse("I can't be mad, I'm not human.", "Nope."), "mad", POS.ADJECTIVE));
				
		//Advanced response example: Are you....
		Response AreYou = new Response() {
			@Override
			public String response(Parse p, HashMap<String, Object> context) {
				
				Parse Noun = null;
				if (Global.findFirstTag(p, new String[] { "NN" }) != null) {
					Noun = Global.findFirstTag(p, new String[] { "NN" });
					if (WordRelations.isHypernymOf(Noun.toString(), POS.NOUN, "computer")) {
						return Global.randomChoice("You got me.", "I guess you could say that.", "Ask Geppetto.");
					}
				}			
				
				if (p.toString().toLowerCase().contains("okay") || p.toString().toLowerCase().contains("ok") ||
						p.toString().toLowerCase().contains("alright")) {
					return (Global.randomChoice("Sure, why do you ask?", "Always!"));
				}
				
				return(Global.randomChoice("Not so much.", "Nope", "No"));
				
			}
		};
		
		ret.add(makeWordMatchEntry(AreYou,"Are you"));
		
		ret.add(makeWordMatchEntry(new BasicResponse("Only if you can get it for yourself."), "Can I get"));
		
		
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
	
	public Entry makeRegexEntry(Response r, String wordPatternRegex) {
		Entry ret = new Entry();
		ret.r = r;
		ret.wordPattern = wordPatternRegex;
		ret.isRegexWordPattern = true;
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
	
	//Basic response --- Just returns a string of text.
	public class BasicRandomResponse implements Response {
		private final LinkedList<String> responsePool = new LinkedList<String>();
		
		public BasicRandomResponse(String ... text) {
			//Populate the response pool
			for(String resp : text) {
				responsePool.add(resp);
			}
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
			
			//Randomly pick from the response pool
			return Global.randomListChoice(responsePool);
		}
	}
	
	
	//Make the random convo starters in case the user wants a new topic.
	public class RandomResponse implements Response {
					@Override
		public String response(Parse p, HashMap<String, Object> context) {
			    
			//Put this into the statements context.. it might be useful later
			if(!context.containsKey("starters")) {
				context.put("starters", new LinkedList<String>());
			}
			
			if(!((HashMap<String,Integer>)context.get("userSentences")).isEmpty()){
				HashMap<String, Integer> sents = ((HashMap<String,Integer>)context.get("userSentences"));
				
				for(String sent : sents.keySet()){
					//Flip possessive statements
					Parse users = Global.parseString(sent);
					
					Parse[] level2 = users.getChildren();
					if (level2[0].getType().equals("S")){
						sent = Global.flipPossesives(sent.toString());		    			    
						sent = "Well, you said "  + sent.substring(0,sent.length()-1) + ", tell me more.";
						
						if (((LinkedList<String>)context.get("starters")).contains(sent)){
							continue;
						}
							
					    ((LinkedList<String>)context.get("starters")).add(sent);
						return Global.randomChoice(sent);     
					}
						
				}
			}
			
			String sent = "How's your family?";
			int tries = 0;
					
			while (((LinkedList<String>)context.get("starters")).contains(sent) && tries <= 20){
					sent = Global.randomChoice("What is your favorite sport?",
					"How do you feel about this freakin weather?",
					"Well what do you wanna talk about?",
					"Do you like sports?",
					"Do you know who I am?",
					"Do you have any siblings?",
					"What is your favorite food?",
					"How old are you?",
					"What did you do today?");
					tries++;
			}
			
			if (tries == 21){
				sent = "I'm tired of coming up with conversation ideas.";
			}
				
			((LinkedList<String>)context.get("starters")).add(sent);
				
			return sent;
		}	
	}
}
