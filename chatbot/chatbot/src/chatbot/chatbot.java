package chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.*;
import opennlp.tools.tokenize.Tokenizer;
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

		while (true) {
			// Parse a sentence
			String sentences[] = sdetector.sentDetect(readInput());
			for (String sent : sentences) {
				Parse[] topParses = ParserTool.parseLine(sent, parser, 3);
				

				for (Parse p : topParses) {
					
					//p.show();
				}
				
				//respond
				System.out.println(response(topParses));
			}
		}
	}
	
	static String response(Parse[] parsedSentences) {
		for(Parse child : parsedSentences[0].getChildren()) {
			child.show();
			System.out.println(child.getType());
			if(child.getType().equals("SBARQ")) {
				return "Why do you care?";
			}
			if(child.getType().equals("S")) {
				Parse p = findFirstTag(child,  new String[] {"SBAR"} ); 
				if(p != null) {
				  return "You truly believe that " + p.toString() + "?";
				}
				
				p = findFirstTag(child,  new String[] {"NN", "NNS", "SBAR"} ); 
				if(p != null) {
					//NN or NNS
					return "Tell me more about the " +  p.toString();
				}
				
			}
			if(child.getType().equals("SQ")) {
				return "The answer is yes. I like yes or no questions.";
			}
		}
		return "";
	}
	
	
	//depth first search on the parse tree that returns the first instance of 
	//parse with getType() of <name>.
	static Parse findFirstTag(Parse tree, String[] names) {
		for(String s : names) {
		  if(tree.getType().equals(s)) {
  			return tree;
	       }
		}
		
        for(Parse child : tree.getChildren()) {
		  Parse p = findFirstTag(child, names);
		   if(p != null) {
		     return p;
		   }
		}
		 
		return null;
	}
}

