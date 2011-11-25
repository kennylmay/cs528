package chatbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

@SuppressWarnings("serial")
public class WordNGram extends Vector<HashMap<String, Integer>>{

	private final int maxNGrams;
	
	public WordNGram() {
		this(4);		
		
	}
	
	public WordNGram(int numNGrams) {
		super();
		
		maxNGrams = numNGrams;
		
		for(int i = 0; i < this.maxNGrams; i++) {
			this.add(i, new HashMap<String, Integer>());
		}
	}
	
	
	public void ProcessFile(String resourceName) {
		
		InputStream inStream;
		BufferedReader buffReader;
		
		String lineString;
		
		try {
			inStream = chatbot.class.getResourceAsStream(resourceName);
			buffReader = new BufferedReader(new InputStreamReader(inStream));
			

			while ((lineString = buffReader.readLine()) != null) {
				ProcessLine(PrepLine(lineString));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public void TruncLowOccur(int minNum) {
		
		for(int i = 0; i < 4; i++) {
			
			System.out.println("Pre - Word list of size " + (i+1) + ": " + this.elementAt(i).size());
			
			Iterator<Entry<String, Integer>> iter = this.elementAt(i).entrySet().iterator();
			
			while(iter.hasNext()) {
				Map.Entry<String, Integer> currentEntry = iter.next();
				if (currentEntry.getValue() < minNum) {
					iter.remove();
				}
			}
			
			
			System.out.println("Post - Word list of size " + (i+1) + ": " + this.elementAt(i).size());
		}
		
		
	}
	
	private void ProcessLine(String line) {

		String[] words = line.split(" ");
		int currentWord;
		String tmpString;

		for (currentWord = words.length; currentWord > 0; currentWord--) {

			tmpString = "";
			for (int ngram = 0; ngram < this.maxNGrams
					&& (currentWord - 1 - ngram) >= 0; ngram++) {

				// Handle the last word in the list as a single.

				tmpString = words[currentWord - 1 - ngram] + " " + tmpString;
				tmpString = tmpString.trim();

				if (!this.elementAt(ngram).containsKey(tmpString)) {
					this.elementAt(ngram).put(tmpString, 1);
				} else {
					this.elementAt(ngram).put(tmpString,
							this.elementAt(ngram).get(tmpString) + 1);
				}
			}
		}
	}

	private String PrepLine(String line) {
		
		line = line.replaceAll("\t", " ");
		line = line.replaceAll("  ", " ");
		line = line.replaceAll("[.?!,\";]", "");
		return line;
		
	}

	@Override
	public String toString() {
		String retVal = "";
		
		retVal = this.elementAt(0).toString() + '\n';
		retVal += this.elementAt(1).toString() + '\n';
		retVal += this.elementAt(2).toString() + '\n';
		retVal += this.elementAt(3).toString() + '\n';
		
		return retVal;
	}
	
	
	
	
	
	
}
