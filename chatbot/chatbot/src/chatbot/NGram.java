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
import opennlp.tools.parser.Parse;


@SuppressWarnings("serial")
public class NGram extends HashMap<String, HashMap<String, Integer>> {

	public class dictionary extends HashMap<String, Vector<String>> {
		public String toString() {
			String string = null;
			for (String s : this.keySet()) {
				string += "Type: " + s + " Examples: \n";
				for (String v : this.get(s)) {
					string += "\t" + v + "\n";
					string += "\n";
				}
				string += "\n\n";
			}

			return string;
		}

		public String toString(final String phrase) {
			String string = "";
			string += "Type: " + phrase + " Examples: \n";
			if (this.get(phrase) == null) {
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
		Parse[] topParse = ParserTool.parseLine(sent, Global.parser, 3);



		// Crawl children
		for (Parse child : topParse) {
			crawl(child);
		}
	}

	public void crawl(Parse p) {
		
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
		if (ent == null) {
			put(topTag, new HashMap<String, Integer>());
			dict.put(topTag, new Vector<String>());
			ent = get(topTag);
		}
		
		
		
		// Add this phrase to the dict of phrases for this POS type.
		if (!dict.get(topTag).contains(p.toString())) {
			dict.get(topTag).add(p.toString());
		}
		
	

		// Construct the entry;
		for (Parse child : children) {
			childTags += child.getType() + " ";
		}
		
		childTags = childTags.trim();
		
		// Increment any existing entry for this leaf NGram
		Integer val = ent.get(childTags);
		if (val == null) {
			val = 0;
		}

		// Put the NGram in
		ent.put(childTags, ++val);

		if (!dict.containsKey(childTags)) {
			dict.put(childTags, new Vector<String>());
		}
		
		if (!dict.get(childTags).contains(p.toString())) {
			dict.get(childTags).add(p.toString());
		}

//		String subsentence = "";
//		for (Parse child : children) {
//			subsentence += child + " ";
//		}
//		
//		sents = dict.get(childTags);
//		if (sents == null) {
//			dict.put(childTags, new Vector<String>());
//			sents = dict.get(childTags);
//		}		
//		sents.add(subsentence);

		
		// Crawl children
		for (Parse child : children) {
			crawl(child);

		}

		/*
		 * // Crawl children for (Parse child : children) { crawl(child); }
		 */
	}

	public String toString() {
		String ret = "";

		// Iterate over top tags
		for (String tag : keySet()) {
			ret += "NGRAM for : " + tag + "\n";
			ret += NGramEnt2Str(get(tag)) + "\n\n";
		}

		return ret;
	}

	public class Pair implements Comparator<Pair> {
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



	
	public static String NGramEnt2Str(HashMap<String, Integer> ent) {
		String ret = "";
		LinkedList<Pair> list = new LinkedList<Pair>();
		for (String name : ent.keySet()) {
			list.add(new NGram().new Pair(name, ent.get(name)));
		}

		// Sort it with descending values
		Collections.sort(list, list.get(0));

		for (Pair p : list) {
			ret += p.name + " : " + p.val + "\n";
		}

		return ret;
	}
	
	public static void doNGrams() {
		InputStream is = chatbot.class.getResourceAsStream("/play");

		NGram ng = new NGram();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String sentence;
			while ((sentence = in.readLine()) != null) {
				sentence = Global.tokenize(sentence, Global.tokenizer);
				ng.parseSentence(sentence);
			}
			System.out.println(ng);
			String Answer = "";
			while (!Answer.toLowerCase().equals("stop")) {
				System.out
						.print("Which parse type would you like examples for? (type 'stop' to quit)");
				try {
					Answer = Global.stdin.readLine();
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
}