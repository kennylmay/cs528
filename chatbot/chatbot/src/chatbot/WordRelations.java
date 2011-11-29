package chatbot;

import java.util.Iterator;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
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
			
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(inputWord.getSense(1), targetWord.getSense(1), PointerType.HYPERNYM);

			if (list.size() == 0){
				return false;
			}
			
			System.out.println("MATCH INDEX: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
			if (((AsymmetricRelationship) list.get(0)).getCommonParentIndex() < 4){
				return true;
			}else{
				return false;
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
				return 0;
			}
			
			IndexWord targetWord;
			targetWord = Dictionary.getInstance().getIndexWord(posType, target);
			
			if (targetWord == null) {
				return 0;
			}
			
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(inputWord.getSense(1), targetWord.getSense(1), PointerType.HYPERNYM);
			
			if (list.size() == 0){
				return 0;
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
}