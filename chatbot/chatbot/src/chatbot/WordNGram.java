package chatbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

@SuppressWarnings("serial")
public class WordNGram extends Vector<HashMap<String, Integer>>{

	
	
	public WordNGram() {
		super();
		
		this.add(0, new HashMap<String, Integer>());
		this.add(1, new HashMap<String, Integer>());
		this.add(2, new HashMap<String, Integer>());
		this.add(3, new HashMap<String, Integer>());
		
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
//System.out.println(this.elementAt(0).toString());
//System.out.println(this.elementAt(1).toString());
//System.out.println(this.elementAt(2).toString());
//System.out.println(this.elementAt(3).toString());

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
		
		// All strings of size 4 or more.
		for (currentWord = words.length ; currentWord >= 4; currentWord--) {
			
			// Handle the last word in the list as a single.
			tmpString = words[currentWord - 1];
			if (!this.elementAt(0).containsKey(tmpString)) {
				this.elementAt(0).put(tmpString, 1);
			} else {
				this.elementAt(0).put(tmpString, this.elementAt(0).get(tmpString) + 1);
			}
			
			//Handle the last two words as a double.
			tmpString = words[currentWord - 2] + " " + tmpString;
			if (!this.elementAt(1).containsKey(tmpString)) {
				this.elementAt(1).put(tmpString, 1);
			} else {
				this.elementAt(1).put(tmpString, this.elementAt(1).get(tmpString) + 1);
			}
			
			//Handle the last three words as a triple.
			tmpString = words[currentWord - 3] + " " + tmpString;
			if (!this.elementAt(2).containsKey(tmpString)) {
				this.elementAt(2).put(tmpString, 1);
			} else {
				this.elementAt(2).put(tmpString, this.elementAt(2).get(tmpString) + 1);
			}
			
			//Handle the last four words as a quad.
			tmpString = words[currentWord - 4] + " " + tmpString;
			if (!this.elementAt(3).containsKey(tmpString)) {
				this.elementAt(3).put(tmpString, 1);
			} else {
				this.elementAt(3).put(tmpString, this.elementAt(3).get(tmpString) + 1);
			}			
			
			
		}
		
		if (currentWord >= 3) {
			// Handle the last word in the list as a single.
			tmpString = words[currentWord - 1];
			if (!this.elementAt(0).containsKey(tmpString)) {
				this.elementAt(0).put(tmpString, 1);
			} else {
				this.elementAt(0).put(tmpString, this.elementAt(0).get(tmpString) + 1);
			}
			
			//Handle the last two words as a double.
			tmpString = words[currentWord - 2] + " " + tmpString;
			if (!this.elementAt(1).containsKey(tmpString)) {
				this.elementAt(1).put(tmpString, 1);
			} else {
				this.elementAt(1).put(tmpString, this.elementAt(1).get(tmpString) + 1);
			}
			
			//Handle the last three words as a triple.
			tmpString = words[currentWord - 3] + " " + tmpString;
			if (!this.elementAt(2).containsKey(tmpString)) {
				this.elementAt(2).put(tmpString, 1);
			} else {
				this.elementAt(2).put(tmpString, this.elementAt(2).get(tmpString) + 1);
			}
			
			currentWord--;
		}
		
		if (currentWord >= 2) {
			// Handle the last word in the list as a single.
			tmpString = words[currentWord - 1];
			if (!this.elementAt(0).containsKey(tmpString)) {
				this.elementAt(0).put(tmpString, 1);
			} else {
				this.elementAt(0).put(tmpString, this.elementAt(0).get(tmpString) + 1);
			}
			
			//Handle the last two words as a double.
			tmpString = words[currentWord - 2] + " " + tmpString;
			if (!this.elementAt(1).containsKey(tmpString)) {
				this.elementAt(1).put(tmpString, 1);
			} else {
				this.elementAt(1).put(tmpString, this.elementAt(1).get(tmpString) + 1);
			}
			
			currentWord--;
		}
		
		if (currentWord >= 1) {
			// Handle the last word in the list as a single.
			tmpString = words[currentWord - 1];
			if (!this.elementAt(0).containsKey(tmpString)) {
				this.elementAt(0).put(tmpString, 1);
			} else {
				this.elementAt(0).put(tmpString, this.elementAt(0).get(tmpString) + 1);
			}
		}
		
		// Last string of size 3.
		
		
		
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
