
public class OrRule extends Rule {
	
	public OrRule(Rule  ... theSubRules) {
		for(Rule r: theSubRules) {
			subRules.add(r);
		}
	}
	@Override
	public boolean eval(LoLItem item) {
		//Or rules are true if any of the subrules are true
		  for(Rule g: subRules) {
			  if(g.eval(item) == true) {
				  return true;
			  }
		  }
		  return false;
	}
	
	private OrRule() {} ;
	
}