package chatbot;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

public class WordRelations {

	public boolean isHypernymOf(String input, POS posType, String targetWord) {

		IndexWord indexWord;
		try {
			indexWord = Dictionary.getInstance().getIndexWord(posType, input);
			if (indexWord == null) {
				return false;
			}

			Synset[] set = indexWord.getSenses();
			Pointer[] pointer = set[0].getPointers(PointerType.HYPERNYM);

			for (Pointer x : pointer) {
				Synset target = x.getTargetSynset();
				if (target.getWord(0).getLemma().equals(targetWord)) {
					return true;
				}
			}

			return false;

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}