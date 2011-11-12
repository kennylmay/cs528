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

	SentenceDetectorME sdetector = null;
	TokenizerME tokenizer = null;

	SentenceModel sm = null;
	TokenizerModel tm = null;	
	ParserModel pm = null;
	
	public void loadModels() {
	

        
		
		try {
			//Sentence model
			InputStream res = chatbot.class
			.getResourceAsStream("/model/en-sent.bin");
			sm = new SentenceModel(res);
			sdetector = new SentenceDetectorME(sm);
			
			
			//Tokenizer model
			res = chatbot.class
			.getResourceAsStream("/model/en-token.bin");
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
	}
	
		

