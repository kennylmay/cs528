package NGrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import chatbot.Global;
import chatbot.chatbot;


public class NGrams {
	public static void main(String[] args) {

		//Uncomment these if you want the POS NGrams (or the POS filtered WordNGrams)
		Global.stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Loading POS models..");
		Global.loadModels();
		POSNGram.doNGrams(chatbot.class.getResourceAsStream("/play"));
		POSNGram.doNGrams(chatbot.class.getResourceAsStream("/play2"));
		POSNGram.doNGrams(chatbot.class.getResourceAsStream("/hack"));
		POSNGram.doNGrams(chatbot.class.getResourceAsStream("/sic"));
		System.out.println("Done");

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WordNGram wg = new WordNGram();
		wg.ProcessFile("/play");
		wg.ProcessFile("/hack");
		wg.ProcessFile("/play2");
		wg.ProcessFile("/sic");
		
		
		
		
		
		for(int i = 0; i < wg.size(); i++) {
			WordNGram.TruncBelowDeviations(wg.get(i),3);
			//WordNGram.TruncBelowPercentile(wg.get(i), 99.5);
			System.out.println("\n\n" + (i+1) + " level NGram:\n------------------------------------------------");
			System.out.println(POSNGram.NGramEnt2Str(wg.get(i)));
			//ArrayList<parseObject> tags = wg.filterPhrases(wg.tagWordNGram(i));
			//System.out.println(wg.arrayListParseToString(tags));
		}
		

		
       
	}
}
