import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Token {
	public enum Typ {
		Player, Attribute, LogicalOp, NoOp
	};

	public enum PlayerTyp {
		Us, Them
	};

	public enum LogicalOpTyp {
		AND, NOT, OR
	};

	public Typ t;
	public Object data;

	public Token(Typ t, Object data) {
		this.t = t;
		this.data = data;
	}

	public String toString() {
		return t.toString() + "(" + data.toString() + ")";
	}

	/*
	 * Grammar
	 * 
	 * 
	 * <Sentence> ::= <Sentence> | LogicalOp <Sentence>
	 * 
	 * <Sentence> ::= Player Attribute | Attribute
	 */

	public static boolean isValidGrammar(LinkedList<Token> tokens) {

		if (tokens == null) {
			return false;
		}
		// Copy off the list
		@SuppressWarnings("unchecked")
		LinkedList<Token> toks = (LinkedList<Token>) tokens.clone();

		// remove NoOps
		Iterator<Token> iter = toks.iterator();
		while (iter.hasNext()) {
			Token t = iter.next();
			if (t.t == Typ.NoOp) {
				iter.remove();
			}
		}

		if (toks.size() == 0) {
			return false;
		}
		
		while (toks.size() > 0) {
			if (!isValidSentence(toks)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isValidSentence(LinkedList<Token> toks) {
		if (toks.size() == 0) {
			return false;
		}

		// Check if this is an attribute sentence
		if (toks.peek().t == Typ.Attribute) {
			// Pop the attribute token
			Token attribute = toks.poll();
			
			// Bad if it's not in the rule dict
			if(ruleFactory.ruleDict.get((String)attribute.data) == null) {
				System.out.println("No rule in dictionary for " +attribute.data);
				return false;
			}
			return true;
		}

		// Check if this is the logicop - sentence type.
		if (toks.peek().t == Typ.LogicalOp) {
			// Pop the logic op token
			toks.poll();
			
			return isValidSentence(toks);
		}

		// Check if it's a player sentence
		if (toks.peek().t == Typ.Player) {
			// Pop the player token
			toks.poll();

			// Pop the attribute token
			Token attribute = toks.poll();
			
			// Bad grammar if no attribute token
			if (attribute == null || attribute.t != Typ.Attribute) {
				return false;
			}
			
			// Bad if it's not in the rule dict
			if(ruleFactory.ruleDict.get((String)attribute.data) == null) {
				System.out.println("No rule in dictionary for " +attribute.data);
				return false;
			}
			
			
			return true;
		}

		return false;
	}

	public static ItemRule tokens2ItemRule(LinkedList<Token> tokens) {
		// Copy off the list
		@SuppressWarnings("unchecked")
		LinkedList<Token> toks = (LinkedList<Token>) tokens.clone();

		LinkedList<ItemRule> rules = new LinkedList<ItemRule>();
		while (toks.size() > 0) {
			rules.add(sentence2Rule(toks));
		}

		return new ItemRule(rules);
	}

	private static ItemRules ruleFactory = new ItemRules();

	private static ItemRule sentence2Rule(LinkedList<Token> toks) {

		System.out.println("Processing " + toks);

		// remove NoOps
		Iterator<Token> iter = toks.iterator();
		while (iter.hasNext()) {
			Token t = iter.next();
			if (t.t == Typ.NoOp) {
				iter.remove();
			}
		}

		// Check if this sentence looks like a player sentence
		if (toks.peek().t != Typ.Player && toks.peek().t != Typ.LogicalOp
				&& toks.peek().t != Typ.Attribute) {
			return null;
		}

		// If this is an attribute sentence, return a rule for it
		if (toks.peek().t == Typ.Attribute) {
			// Pop the attribute token
			Token attribute = toks.poll();
			
			return ruleFactory.ruleDict.get((String)attribute.data);
		}

		// Check if this is the logicop - sentence type. If it is, recurse on
		// this with the appropriate rule.
		if (toks.peek().t == Typ.LogicalOp) {
			// Pop the logic op token
			Token logicOp = toks.poll();
			if (logicOp.data == LogicalOpTyp.NOT) {
				return ruleFactory.Not(sentence2Rule(toks));
			} else if (logicOp.data == LogicalOpTyp.AND) {
				return sentence2Rule(toks);
			} else {
				// Unknown logic op, return null
				return null;
			}
		}

		// Check if it's a player sentence
		if (toks.peek().t == Typ.Player) {
			// Pop the player token
			Token player = toks.poll();

			// Pop the attribute token
			Token attribute = toks.poll();
			// Bad grammar if no attribute token
			if (attribute == null) {
				return null;
			}

			// If it's them, build a rule for counters(attr)
			if (player.data == PlayerTyp.Them) {
				LinkedList<ItemRule> agg = new LinkedList<ItemRule>();
				LinkedList<String> counters = Expert
						.countersFor((String) attribute.data);
				System.out.println("Counters for " + attribute.data + " are : "
						+ counters);
				for (String c : counters) {
					agg.add(new ItemRule(ruleFactory.ItemHas(c)));
				}
				return new OrItemRule(agg);
			}

			// It's us, build a rule for the attr
			else {
				if(ruleFactory.ruleDict.get((String)attribute.data) == null) {
					System.out.println("Could not build rule for " + attribute.data);
					return null;
				}
				return 	ruleFactory.ruleDict.get((String)attribute.data);
			}

		}

		// Bad Grammar if we get here
		return null;
	}
}
