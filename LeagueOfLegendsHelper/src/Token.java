import java.util.Collections;
import java.util.LinkedList;

public class Token {
  public enum Typ { Player, Attribute, LogicalOp, RelationalOp, NoOp };
  public Typ t;
  public Object data;
  public String rawText;
  
  public Token(Typ t, Object data, String rawText) {
    this.t = t;
    this.data = data;
    this.rawText = rawText;
  }

  
  /* Grammar
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


private ItemRule sentence2Rule(LinkedList<Token> toks) {
	// TODO Auto-generated method stub
	return null;
}
}
