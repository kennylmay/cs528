package chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class Global {
	public static SentenceDetectorME sdetector = null;
	public static TokenizerME tokenizer = null;
	public static SentenceModel sm = null;
	public static TokenizerModel tm = null;
	public static Dictionary dict = null;
	public static ParserModel pm = null;
	public static Parser parser = null;
	public static POSModel posModel = null;
	public static POSTaggerME tagger = null;
	public static BufferedReader stdin = null;
	
	public static void loadModels() {

		try {
			// Sentence model
			InputStream res = chatbot.class.getResourceAsStream("/en-sent.bin");

			sm = new SentenceModel(res);
			sdetector = new SentenceDetectorME(sm);

			// Tokenizer model
			res = chatbot.class.getResourceAsStream("/en-token.bin");
			tm = new TokenizerModel(res);
			tokenizer = new TokenizerME(tm);

			// POSModel
			res = chatbot.class.getResourceAsStream("/en-pos-maxent.bin");
			posModel = new POSModel(res);
			tagger = new POSTaggerME(posModel);

			// ParserModel
			res = chatbot.class.getResourceAsStream("/en-parser-chunking.bin");
			pm = new ParserModel(res);
			parser = ParserFactory.create(pm);

		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String tokenize(String sent, TokenizerME tokenizer) {
		String tokens[] = tokenizer.tokenize(sent);
		String tokenizedSent = "";
		for(String t : tokens) {
			if(t.equals(",") || t.equals(".") || t.equals("?") || t.equals("\"") || t.equals("'")) {
			   continue;
			}
			tokenizedSent += t + " ";
		}
		tokenizedSent = tokenizedSent.substring(0, tokenizedSent.length() - 1);
		return tokenizedSent;
	}
	
	public static String readInput() {

		System.out.print("\nWhat are you thinking???\n\n");

		String retString = "";

		try {
			retString = stdin.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error reading input!");
			System.exit(1);
		}

		return retString;
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
				if(Global.opposites.get(bite) != null) {
					//If it is, add it as a ret token and remove the bites
					retTokens.add(Global.opposites.get(bite));
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

	private static String serialize(Parse[] p) {
		String ret = "";
		// Construct the entry;
		for (Parse child : p) {
		   ret += child.getType() + " ";
		}
	     ret = ret.trim();
		return ret;
	}

	public static Collection<String> sortedByLargestFirst(Collection<String> col) {
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

	static HashMap<String,String> opposites = new HashMap<String,String>();
	static LinkedList<String> oppositeKeys = new LinkedList<String>();
	static int maxBite = 0;
	static {
    	Global.opposites.put("i am", "you are");
    	Global.opposites.put("i", "you");
    	Global.opposites.put("you", "me");
    	Global.opposites.put("my", "your");
    	Global.opposites.put("mine", "yours");
    	Global.opposites.put("you are", "i am");
    	
    	//Put in reverse order pairs
    	LinkedList<String> keys = new LinkedList<String>();
    	for(String key : Global.opposites.keySet()) {
    		keys.add(key);
    	}
    	for(String key : keys) {
            Global.opposites.put(Global.opposites.get(key),key);
    	}
    	for(String key: Global.opposites.keySet()) {
    		oppositeKeys.add(key);
    	}

    	//Get the max bite
    	for(String key : Global.sortedByLargestFirst(oppositeKeys)) {
    		maxBite = Math.max(maxBite, key.split(" ").length);
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

	public static LinkedList<Parse> findAllTags(Parse tree, String[] names) {
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

	//Increments a key in a hashmap<string,int>
	public static void incrementKey(HashMap<String, Integer> hashMap, String key) {
		if(hashMap.containsKey(key)) {
			Integer val = hashMap.get(key);
			val++;
			hashMap.put(key, val);
		}
		else {
			hashMap.put(key, new Integer(1));
		}
		
	}
	
	
	//Randomly pick one of the arguments
	public static Random rand = new Random();
	public static <T> T randomChoice(T ... choices) {
		int val = Math.abs(rand.nextInt() % choices.length);
		int i = 0;
		for(T choice : choices) {
			if(val == i++) {
				return choice;
			}
		}
		//Will never get here
		return null;
	}
	
	public static boolean beginsWithVowel (String s)
	{
	 return ( s.startsWith ("a") || s.startsWith ("e") || s.startsWith ("i") || s.startsWith ("o") || s.startsWith ("u") || 
		   	  s.startsWith ("A") || s.startsWith ("E") || s.startsWith ("I") || s.startsWith ("O") || s.startsWith ("U"));
	}

}
