package chatbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.tokenize.TokenizerME;


public class chatbot {



	public static void main(String[] args) {
		System.out.println("Initializing...");
		Global.loadModels();
		// open up standard input
		Global.stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Done");

		chatbot cb = new chatbot();
		//NGram.doNGrams();
		cb.startDialogLoop();
        
	}

	
	public void startDialogLoop() {
	     Responders r = new Responders();
		 while (true) { 
			// Parse a sentence 
			String sentences[] = Global.sdetector.sentDetect(Global.readInput());
			if(sentences == null || sentences.length == 0) {
				continue;
			}
			String sent = sentences[0];
			sent = Global.tokenize(sent, Global.tokenizer);
			
		    Parse[] topParses = ParserTool.parseLine(sent, Global.parser, 1);

		    Parse p = topParses[0];
		    
	  	    p.show();
		  
		    //respond 
		    System.out.println(r.response(p));
		 }
	}




}
