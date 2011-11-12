package chatbot;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.parser.ParserModel;
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
	public static ParserModel pm = null;
	
	public static void loadModels() {
	

        
		
		try {
			//Sentence model
			InputStream res = chatbot.class
			.getResourceAsStream("/en-sent.bin");
			
			sm = new SentenceModel(res);
			sdetector = new SentenceDetectorME(sm);
			
			
			//Tokenizer model
			res = chatbot.class
			.getResourceAsStream("/en-token.bin");
			tm = new TokenizerModel(res);
			tokenizer = new TokenizerME(tm);
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	public static void main(String[] args) {
		loadModels();
		sdetector.sentDetect("Hello world. Hello world again.");
		
	}
	
}
	
		

