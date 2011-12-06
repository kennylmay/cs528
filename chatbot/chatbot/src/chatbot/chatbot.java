package chatbot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import NGrams.POSNGram;

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
		
		new UserInterface();
		System.out.println("Initializing...");
		Global.loadModels();
		try {
			JWNL.initialize(chatbot.class.getResourceAsStream("/file_properties.xml"));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		// open up standard input
		Global.stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Done");

		//Start the dialog loop
		new chatbot().startDialogLoop();
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
			sent = Global.prepLine(sent);

			Parse[] topParses = ParserTool.parseLine(sent, Global.parser, 1);

			Parse p = topParses[0];

			p.show();

			// respond
			System.out.println(r.response(p, context));
		}
	}
}
