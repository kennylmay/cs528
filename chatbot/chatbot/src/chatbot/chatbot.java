package chatbot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;

public class chatbot {

	public static void main(String[] args) {
		
		System.out.println("Initializing...");
		
		try {
			JWNL.initialize(chatbot.class.getResourceAsStream("/file_properties.xml"));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		// open up standard input
		Global.loadModels();
		Global.stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Done");
		
		//chatbot cb = new chatbot();
		//NGram.doNGrams();
		 
		//cb.startDialogLoop();

		testMorphological();
        	
		
		new chatbot().startDialogLoop();
		
		WordNGram wg = new WordNGram();
		wg.ProcessFile("/happy.txt");
		//wg.TruncLowOccur(4);
		
		System.out.println("Done");
		
		for(int i = 0; i < wg.size(); i++) {
			WordNGram.TruncBelowDeviations(wg.get(i),3);
			//WordNGram.TruncBelowPercentile(wg.get(i), 99.5);
			System.out.println("\n\n" + (i+1) + " level NGram:\n------------------------------------------------");
			System.out.println(NGram.NGramEnt2Str(wg.get(i)));
			ArrayList<parseObject> tags = wg.filterPhrases(wg.tagWordNGram(i));
			System.out.println(wg.arrayListParseToString(tags));
		}
		
	}

	public void startDialogLoop() {
		Responders r = new Responders();
		HashMap<String,Object> context = new HashMap<String,Object>();
		while (true) {
			// Parse a sentence
			String sentences[] = Global.sdetector
					.sentDetect(Global.readInput());
			if (sentences == null || sentences.length == 0) {
				continue;
			}
			String sent = sentences[0];
			sent = Global.tokenize(sent, Global.tokenizer);

			Parse[] topParses = ParserTool.parseLine(sent, Global.parser, 1);

			Parse p = topParses[0];

			p.show();

			// respond
			System.out.println(r.response(p, context));
		}
	}
	
	public static void testMorphological() {
		try {  
			
			IndexWord indexWord = Dictionary.getInstance().getIndexWord(POS.NOUN, "dog");
			Synset[] set = indexWord.getSenses();
			Pointer[] pointerA = set[0].getPointers(PointerType.HYPONYM);
			Pointer[] pointerB = set[0].getPointers(PointerType.HYPERNYM);
			
			System.out.println("\nHYPONYMS FOR " + indexWord.getLemma());
			
			for (Pointer x : pointerA) {
				Synset target = x.getTargetSynset();
				System.out.println(target.getWord(0).getLemma());
			}
			
			System.out.println("\nHYPERNYMS FOR " + indexWord.getLemma());
			
			for (Pointer x : pointerB) {
				Synset target = x.getTargetSynset();
				System.out.println(target.getWord(0).getLemma());
				
				IndexWord indexWord2 = Dictionary.getInstance().getIndexWord(target.getPOS(), target.getWord(0).getLemma());
				if (indexWord2 == null)
					break;
				
				System.out.println("\nHYPERNYMS FOR " + indexWord2.getLemma());
				Synset[] set2 = indexWord2.getSenses();
				Pointer[] pointer2 = set2[0].getPointers(PointerType.HYPERNYM);
				
				
				for (Pointer y : pointer2) {
					Synset target2 = y.getTargetSynset();
					System.out.println(target2.getWord(0).getLemma());
				}
			}
			
            
		
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	


}
