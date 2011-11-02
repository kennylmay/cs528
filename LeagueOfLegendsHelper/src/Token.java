import java.util.Collections;
import java.util.LinkedList;

public class Token {
  public enum Typ { Player, Attribute, LogicalOp, NoOp };
  public Typ t;
  public Object data;
  
  public Token(Typ t, Object data) {
    this.t = t;
    this.data = data;
  }

  public String toString() {
	  return t.toString() + "(" + data.toString() + ")";
  }
  /* Grammar
   * 
   
   <Sentence> ::= <Sentence> | LogicalOp <Sentence>
   
   <Sentence> ::= Player Attribute | Attribute 
   
   */
  
  
  
  
  /* Future Grammar.. Maybe...
   * 
   
   <Sentence> ::= <Sentence> | LogicalOp <Sentence>
   
   <Sentence> ::= <PlayerSentence> | Attribute | <RelationAndAttribute>
   
   <PlayerSentence> ::=  Player Attribute | Player <RelationAndAttribute>
   
   <RelationAndAttribute> ::= RelationalOp Attribute
   
   
   */
  
  public ItemRule tokens2ItemRule(LinkedList<Token> tokens) {
	  //Copy off the list
	  LinkedList<Token> toks =  new LinkedList<Token>();
	  Collections.copy(toks, tokens);
	  
	  LinkedList<ItemRule> rules = new LinkedList<ItemRule>();
	  while(toks.size() > 0) {
		  ItemRule r = sentence2Rule(toks);
	  }
	  

	  return null;
  }


private static ItemRules ruleFactory = new ItemRules();
private ItemRule sentence2Rule(LinkedList<Token> toks) {
	
	//Check if this sentence looks like a player sentence
	if(toks.peek().t != Typ.Player &&  toks.peek().t != Typ.LogicalOp ) {
		return null;
	}
	
	//Aggregate for multiple rules
	LinkedList<ItemRule> agg = new LinkedList<ItemRule>();
	
	//Check if this is the logicop - sentence  type. If it is, recurse on this with the appropriate rule.
	if(toks.peek().t != Typ.LogicalOp) {
		//Pop the logic op token
		Token logicOp = toks.poll();
		if(logicOp.data.equals("not")) {
		  return ruleFactory.Not(sentence2Rule(toks));
		}
		else {
		  //Unknown logic op, return null
		  return null;	
		}
	}
	
	//Check if it's a player sentence
	
	
	
	
 	return null;
}
}
