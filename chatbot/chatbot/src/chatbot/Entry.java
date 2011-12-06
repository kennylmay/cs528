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


	    public boolean isRegexWordPattern = false;
		public String wordPattern = "";
		public String hypernymPattern = "";
		public POS pos;
		public Response r;
		public boolean wordMatchesSentence(Parse p) {
			if(wordPattern.length() == 0) {
				return false;
			}
			
			if(isRegexWordPattern) {
				return p.toString().toLowerCase().matches(wordPattern.toLowerCase());
			}
			else {
			  return p.toString().toLowerCase().contains(wordPattern.toLowerCase());
			}
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
					System.out.println("Matched");
					return true;
				}
		      }
			}
			
			if(pos == POS.ADJECTIVE) {
				  //Get all tags for nouns
				  LinkedList<Parse> adjs = Global.findAllTags(p,new String[] {"JJ", "JJR", "JJS", "VBN"});
				  
				  //Try each adjective
				  for(Parse adj : adjs) {
					  System.out.println("Trying adj: " + adj + " hypermatched to : " + hypernymPattern + " with POS: " + pos);
					if(WordRelations.isHypernymOf(adj.toString(), POS.ADJECTIVE, hypernymPattern)) {
						System.out.println("Matched");
						return true;
					}
			      }
			}
		 return false;
		}
		
		public int topHyperMatchInSentence(Parse p) {
			if(hypernymPattern.length() == 0) {
				return Integer.MAX_VALUE;
			}
			
			int best = Integer.MAX_VALUE;
			if(pos == POS.NOUN) {
			  //Get all tags for nouns
			  LinkedList<Parse> nouns = Global.findAllTags(p,new String[] {"NN", "NNS"});
			
			  //Try each noun
			   best= Integer.MAX_VALUE;
			  for(Parse noun : nouns) {
				  System.out.println("Trying noun: " + noun + " hypermatched to : " + hypernymPattern + " with POS: " + pos);
				  int score = WordRelations.hypernymScore(noun.toString(), POS.NOUN, hypernymPattern);
    			  best =  Math.min(best, score);
			  }
			  
		    }
			
			return best;
		}
		    
	
}