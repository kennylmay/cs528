public class Token {
  public enum Typ { Player, Attribute, LogicalOp, NoOp };
  public Typ t;
  public Object data;
  public String rawText;
  
  public Token(Typ t, Object data, String rawText) {
    this.t = t;
    this.data = data;
    this.rawText = rawText;
  }

}
