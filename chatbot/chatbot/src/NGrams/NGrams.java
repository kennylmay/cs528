package NGrams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


import chatbot.Global;


public class NGrams {
	public static void main(String[] args) {

		//Uncomment these if you want the POS NGrams (or the POS filtered WordNGrams)
		/*
		System.out.println("Loading POS models..");
		Global.loadModels();
		POSNGram.doNGrams();
		System.out.println("Done");
		*/
		
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
