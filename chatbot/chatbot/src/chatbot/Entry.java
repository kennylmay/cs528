package chatbot;

import java.util.LinkedList;

import net.didion.jwnl.data.POS;
import opennlp.tools.parser.Parse;

public class Entry {
		/**
	 * 
	 */

	/**
	 * @param responders
	 */


		public String wordPattern = "";
		public String hypernymPattern = "";
		public POS pos;
		public Response r;
		public boolean wordMatchesSentence(Parse p) {
			if(wordPattern.length() == 0) {
				return false;
			}
			return p.toString().contains(wordPattern);
		}
		
		public boolean hypernymMatchesSentence(Parse p) {
			if(hypernymPattern.length() == 0) {
				return false;
			}
			
			if(pos == POS.NOUN) {
			  //Get all tags for nouns
			  LinkedList<Parse> nouns = Global.findAllTags(p,new String[] {"NN", "NNS"});
			
			  //Try each noun
			  for(Parse noun : nouns) {
				  System.out.println("Trying noun: " + noun + " hypermatched to : " + hypernymPattern + " with POS: " + pos);
				if(WordRelations.isHypernymOf(noun.toString(), POS.NOUN, hypernymPattern)) {
					return true;
				}
		      }
			}
		 return false;
		}
		
		public int topHyperMatchInSentence(Parse p) {
			if(hypernymPattern.length() == 0) {
				return 0;
			}
			
			if(pos == POS.NOUN) {
			  //Get all tags for nouns
			  LinkedList<Parse> nouns = Global.findAllTags(p,new String[] {"NN", "NNS"});
			
			  //Try each noun
			  int best = 0;
			  for(Parse noun : nouns) {
				  System.out.println("Trying noun: " + noun + " hypermatched to : " + hypernymPattern + " with POS: " + pos);
    			  best = Math.max(best, WordRelations.hypernymScore(noun.toString(), POS.NOUN, hypernymPattern));
			  }
			  return best;
		    }
			return 0;
		}
		    
	
}