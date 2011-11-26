package chatbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.rank.Max;

import chatbot.NGram.Pair;

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
		
		for(int i = 0; i < this.maxNGrams; i++) {
			
			//System.out.println("Pre - Word list of size " + (i+1) + ": " + this.elementAt(i).size());
			
			Iterator<Entry<String, Integer>> iter = this.elementAt(i).entrySet().iterator();
			
			while(iter.hasNext()) {
				Map.Entry<String, Integer> currentEntry = iter.next();
				if (currentEntry.getValue() < minNum) {
					iter.remove();
				}
			}
			
			
			//System.out.println("Post - Word list of size " + (i+1) + ": " + this.elementAt(i).size());
		}
		
	}

	public static void TruncBelowPercentile(HashMap<String,Integer> ngram, double percentile) {

		LinkedList<Pair> list = new LinkedList<Pair>();
		for (String name : ngram.keySet()) {
			list.add(new NGram().new Pair(name, ngram.get(name)));
		}
		
		// Sort it by descending values
		Collections.sort(list, list.get(0));

		//Get the cutoff point
		long cutoff = Math.round( (100.0 - percentile) * ngram.size());
		
		//Cut everything greater than cutoff
		Iterator<String> iter = ngram.keySet().iterator();
		int i = 0;
		while(iter.hasNext()) {
			String name = iter.next();
			if(i++ > cutoff) {
				iter.remove();
			}
		}
		
	}
	
	public static void TruncBelowDeviations(HashMap<String,Integer> ngram, double numDeviations) {

		//Calc the standard deviation
		double dbls[] = new double[ngram.size()];
		int i = 0;
		for(Integer val : ngram.values()) {
			dbls[i++] = val;
		}
		double std = new StandardDeviation().evaluate(dbls);
		
		//Get the mean
		double mean = new Mean().evaluate(dbls);
		
		
		//Get the max
		double max = new Max().evaluate(dbls);

		// http://mathforum.org/library/drmath/view/52720.html
		Iterator<String> iter = ngram.keySet().iterator();
		while(iter.hasNext()) {
			String name = iter.next();
			Integer val = ngram.get(name);
			
			//Get the deviation of this entry
			double deviation = (val - mean) / std;
			//System.out.println(val + " has deviation of " + deviation);
			if(deviation < numDeviations) {
				iter.remove();
			}
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
		
		
		for(int i = 0; i < this.maxNGrams; i++) {
			retVal += this.elementAt(i).toString() + '\n';
			
		}
		
		return retVal;
	}
	
	public String toString(int ngram) {
		String retVal = "";
		Iterator<Entry<String, Integer>> iter = this.elementAt(ngram).entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<String, Integer> currentEntry = iter.next();
			retVal += currentEntry.getKey() + "," + currentEntry.getValue() + '\n'; 
		}
		
		return retVal;
	}
	
	
	
	
	
	
}
