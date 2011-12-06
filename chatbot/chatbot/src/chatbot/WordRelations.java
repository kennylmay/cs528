package chatbot;

import java.util.Iterator;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.AsymmetricRelationship;

public class WordRelations {


	public static boolean isHypernymOf(String input, POS posType, String target) {


		try {
			IndexWord inputWord;
			inputWord = Dictionary.getInstance().getIndexWord(posType, input);
			if (inputWord == null) {
				return false;
			}
			
			IndexWord targetWord;
			targetWord = Dictionary.getInstance().getIndexWord(posType, target);
			
			if (targetWord == null) {
				return false;
			}
			
			System.out.println("TARGET WORD" + targetWord.toString());
			
			Synset[] targetsyns = targetWord.getSenses();			
			for (Synset syn : targetsyns){
				for(Word word : syn.getWords()){
					System.out.println(" TRYING! " + inputWord.getLemma() + " =? "+ word.getLemma());
					if(inputWord.getLemma().equals(word.getLemma())){
						System.out.println("FOUND! " + word.getLemma());
						return true;
					}
				}
			}
			
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(inputWord.getSense(1), targetWord.getSense(1), PointerType.HYPERNYM);
			
			if (list.size() == 0){
				return false;
			}
			int RelativeTargetDepth = Math.abs(((AsymmetricRelationship) list.get(0)).getRelativeTargetDepth());
			int TreeDepth =((AsymmetricRelationship) list.get(0)).getDepth();
			
			System.out.println(((AsymmetricRelationship)list.get(0)));
			System.out.println("TREE DEPTH: " + TreeDepth);
			System.out.println("RELATIVE TARGET DEPTH: " + RelativeTargetDepth);
			// If the target is a direct parent of the source return the tree depth;
			if (RelativeTargetDepth == TreeDepth){
				return true;
			}

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	public static int hypernymScore(String input, POS posType, String target) {
		try {
			IndexWord inputWord;
			inputWord = Dictionary.getInstance().getIndexWord(posType, input);
			if (inputWord == null) {
				return Integer.MAX_VALUE;
			}
			
			IndexWord targetWord;
			targetWord = Dictionary.getInstance().getIndexWord(posType, target);
			
			if (targetWord == null) {
				return Integer.MAX_VALUE;
			}
			System.out.println("TARGET WORD" + targetWord.toString());
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(inputWord.getSense(1), targetWord.getSense(1), PointerType.HYPERNYM);
			
			if (list.size() == 0){
				return Integer.MAX_VALUE;
			}
			int RelativeTargetDepth = Math.abs(((AsymmetricRelationship) list.get(0)).getRelativeTargetDepth());
			int TreeDepth =((AsymmetricRelationship) list.get(0)).getDepth();
			
			System.out.println(((AsymmetricRelationship)list.get(0)));
			System.out.println("TREE DEPTH: " + TreeDepth);
			System.out.println("RELATIVE TARGET DEPTH: " + RelativeTargetDepth);
			// If the target is a direct parent of the source return the tree depth;
			if (RelativeTargetDepth == TreeDepth){
				return TreeDepth;
			}		
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public static String getDefinition(String input, POS posType) {
		try {
			IndexWord inputWord;
			inputWord = Dictionary.getInstance().getIndexWord(posType, input);
			if (inputWord == null) {
				return "not familiar to me";
			}
			
			String Sense =  inputWord.getSense(1).toString();
			String Definition = Sense.substring(Sense.indexOf("(") + 1, Sense.indexOf(")")).replace("(", "");
			if (Definition.contains(";")){
			   Definition = Definition.substring(0,Definition.indexOf(";"));
			}
			return (Definition);

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "not familiar to me";
	}
}