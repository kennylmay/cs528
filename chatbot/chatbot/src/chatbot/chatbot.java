package chatbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.*;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class chatbot {

	public static SentenceDetectorME sdetector = null;
	public static TokenizerME tokenizer = null;

	public static SentenceModel sm = null;
	public static TokenizerModel tm = null;
	public static Dictionary dict = null;
	public static ParserModel pm = null;
	public static Parser parser = null;
	public static POSModel posModel = null;
	public static POSTaggerME tagger = null;

	private static BufferedReader stdin = null;

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

	public static void main(String[] args) {
		System.out.println("Initializing...");
		loadModels();
		// open up standard input
		stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Done");
		
		

		chatbot cb = new chatbot();
		//cb.doNGrams();
		cb.startDialogLoop();
        
	}

	public void doNGrams() {
		InputStream is = chatbot.class.getResourceAsStream("/play");
		NGram ng = new chatbot().new NGram();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String sentence;
			while ((sentence = in.readLine()) != null) {
				sentence = tokenize(sentence, tokenizer);
				ng.parseSentence(sentence);
			}
			System.out.println(ng);
			String Answer = "";
			while(!Answer.toLowerCase().equals("stop")){
				System.out.print("Which parse type would you like examples for? (type 'stop' to quit)");
				try {
					Answer = stdin.readLine();
					System.out.println(ng.dict.toString(Answer.toUpperCase()));
				} catch (IOException ioe) {
					System.out.println("IO error reading input!");
					System.exit(1);
				}	
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void startDialogLoop() {
	     Responders r = new Responders();
		 while (true) { 
			// Parse a sentence 
			String sentences[] = sdetector.sentDetect(readInput());
			String sent = sentences[0];
			sent = tokenize(sent, tokenizer);
			
		    Parse[] topParses = ParserTool.parseLine(sent, parser, 1);
  
		   
    

		    Parse p = topParses[0];
		    
	  	    p.show();
		  
		    //respond 
		    System.out.println(r.response(p));
		 }
		
	}
	static String response(Parse[] parsedSentences) {
		for (Parse child : parsedSentences[0].getChildren()) {
			child.show();
			System.out.println(child.getType());
			if (child.getType().equals("SBARQ")) {
				return "Why do you care?";
			}
			if (child.getType().equals("S")) {
				Parse p = Responders.findFirstTag(child, new String[] { "SBAR" });
				if (p != null) {
					return "You truly believe that " + p.toString() + "?";
				}

				p = Responders.findFirstTag(child, new String[] { "NN", "NNS", "SBAR" });
				if (p != null) {
					// NN or NNS
					return "Tell me more about the " + p.toString();
				}

			}
			if (child.getType().equals("SQ")) {
				return "The answer is yes. I like yes or no questions.";
			}
		}
		return "";
	}


	public class NGram extends HashMap<String, HashMap<String, Integer>> {

		private class dictionary extends HashMap<String, Vector<String>> {
			public String toString() {
				String string = null;
				for (String s : this.keySet()) {
					string += "Type: " + s + " Examples: \n";
					for (String v : this.get(s)) {
						string +="\t" + v + "\n";
						string += "\n";
					}		
					string += "\n\n";
				}

				return string;
			}

			public String toString(final String phrase) {
				String string = "";
				string += "Type: " + phrase + " Examples: \n";
				if (this.get(phrase) == null){
					return "Nothing Found";
				}
				for (String v : this.get(phrase)) {
					string += "\t" + v + "\n";
				}
				string += "\n\n";

				return string;
			}
		}

		public dictionary dict = new dictionary();

		public void parseSentence(String sent) {
			// Parse the sentence
			Parse[] topParse = ParserTool.parseLine(sent, parser, 3);


			// Crawl children
			
			for (Parse child : topParse) {
				Parse.pruneParse(child);
				crawl(child);
			}
		}

		public void crawl(Parse p) {
			Parse.pruneParse(p);
			String topTag = p.getType();
			String childTags = "";
			HashMap<String, Integer> ent = null;
			Vector<String> sents = null;
			Parse[] children = null;

			if (p.isFlat() || p.toString().equals(".")) {
				return;
			}

			// Get the children of this node
			children = p.getChildren();
			if (children == null || children.length == 0) {
				return;
			}

			// Check for existence of entry for this parse's N-Gram
			// If it doesn't exist, make it.
			ent = get(topTag);
			sents = dict.get(topTag);
			if (ent == null) {
				put(topTag, new HashMap<String, Integer>());
				dict.put(topTag, new Vector<String>());
				ent = get(topTag);
				sents = dict.get(topTag);
			}

			// Construct the entry;
			for (Parse child : children) {
				childTags += child.getType() + ",";
			}

			// Increment any existing entry for this leaf NGram
			Integer val = ent.get(childTags);
			if (val == null) {
				val = 0;
			}

			// Put the NGram in
			//System.out.println("entry for " + childTags);
			//System.out.println(childTags.length());
			ent.put(childTags, ++val);

			String subsentence = "";
			for (Parse child : children) {
				subsentence += child + " ";
			}
			sents.add(subsentence);

			// Crawl children
			for (Parse child : children) {
				crawl(child);
			}

		}

		public String toString() {
			String ret = "";
			HashMap<String, Integer> ent = null;

			// Iterate over top tags
			for (String tag : keySet()) {
				ret += "NGRAM for : " + tag + "\n";
				ret += NGramEnt2Str(get(tag)) + "\n\n";
			}

			return ret;
		}

		class Pair implements Comparator<Pair> {
			public final Integer val;
			public final String name;

			public Pair(String name, int val) {
				this.name = name;
				this.val = val;
			}

			// Sort high to high
			@Override
			public int compare(Pair o1, Pair o2) {
				return o2.val.compareTo(o1.val);
			}
		}

		public String NGramEnt2Str(HashMap<String, Integer> ent) {
			String ret = "";
			LinkedList<Pair> list = new LinkedList<Pair>();
			for (String name : ent.keySet()) {
				list.add(new Pair(name, ent.get(name)));
			}

			// Sort it with descending values
			Collections.sort(list, list.get(0));

			for (Pair p : list) {
				ret += p.name + " : " + p.val + "\n";
			}

			return ret;
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
}
