package chatbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.data.relationship.SymmetricRelationship;
import net.didion.jwnl.dictionary.Dictionary;
import chatbot.TestDefaults;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;

public class chatbot {

	public static void main(String[] args) {
		System.out.println("Initializing...");
		Global.loadModels();
		// open up standard input
		Global.stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Done");

		chatbot cb = new chatbot();
		// NGram.doNGrams();
		// cb.startDialogLoop();

		testMorphological();
	}

	public void startDialogLoop() {
		Responders r = new Responders();
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
			System.out.println(r.response(p));
		}
	}
	
	public static void testMorphological() {
		try {  
			JWNL.initialize(TestDefaults.getInputStream());
			IndexWord iw = Dictionary.getInstance().lookupIndexWord(POS.VERB, "running-away");
			System.out.println("Index word : " + iw.toString());
			System.out.println("Senses for the word");
			for (Synset sense : iw.getSenses()){
          		System.out.println(sense.toString());
          	}
			
			
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}

}
