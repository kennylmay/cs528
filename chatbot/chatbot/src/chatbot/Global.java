package chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.dictionary.Dictionary;
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

}
